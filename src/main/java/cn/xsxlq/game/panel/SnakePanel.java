package cn.xsxlq.game.panel;

import cn.xsxlq.game.bfs.MaxStep;
import cn.xsxlq.game.bfs.MinStep;
import cn.xsxlq.game.pojo.Point;
import cn.xsxlq.game.pojo.StepInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author wangjs6
 * @version 1.0
 * @Description: 画布
 * @date: 2019/9/13 16:18
 */
public class SnakePanel extends JPanel implements KeyListener, ActionListener {

    /*
     *实现步骤：
     * 1.画布
     * 2.贪吃蛇静态
     * 3.移动
     * 4.控制和死亡
     * 5.随机食物
     * 6.吃食物
     * 7.更新长度
     * 8.积分
     * 9.无脑自动吃食物
     * 10无敌吃食物
     * 11.BFS
     */
    // 方块长度
    private final int cellWidth = 20;
    // snake 长度
    private int len;
    // snake 初始长度
    private int initLen = 3;
    // snake 初始位置
    private final int initSnakeX = 5;
    private final int initSnakeY = 5;

    // 地图大小
    private final int mapLenX = 50;
    private final int mapLenY = 40;

    // 是否游戏
    private boolean isRun = true;

    //是否结束
    private boolean isOver = false;

    //是否win
    private boolean isWin = false;

    // 最小剩余空格数
    private int minWhitePoint = 0;

    // Snake方向
    private String snakeDir;

    private String fontName = "宋体";

    private String stopTips = "STOP";

    private String overTips = "GAME OVER";

    private String winTips = "WIN";


    Timer timer = new Timer(1,this);


    // 为了添加 snake_AI,添加listSnake
    private LinkedList<Point> snakeList = new LinkedList<>();

    // 计算路径
    private MinStep sdfMinStep = new MinStep();
    private MaxStep sdfMaxStep = new MaxStep();

    private int[] snakeX = new int[mapLenX*mapLenY];
    private int[] snakeY = new int[mapLenY*mapLenX];

    // 食物位置
    private int pointX;
    private int pointY;

    public SnakePanel() {
        // 初始化Snake位置
        initSnake();
        // 初始化食物位置
        initPoint();
        // Panel焦点
        this.setFocusable(true);
        // Panel添加键盘监听
        this.addKeyListener(this);
        // 定时器
        timer.start();
    }

    public void paint(Graphics g){
        // 画地图
        paintMap(g);
        // 画Snake
        paintSnake(g);

        if(isRun){
            timer.start();
            isOver = false;
            isWin = false;
            paintPoint(g);
        }else{
            timer.stop();
            if(isWin){
                g.setColor(Color.WHITE);
                showTips(g,winTips);
                initSnake();
                initPoint();
            }else if(isOver){
                showTips(g,overTips);
                initSnake();
                initPoint();
            }else{
                showTips(g,stopTips);
            }
        }
    }

    /**
     * 弹出提示(计算让提示居中显示)
     * @param g
     * @param tips
     */
    private void showTips(Graphics g,String tips){
        int fontSize = 50;
        int fontTop = 240;
        int space = 3;
        g.setFont(new Font(fontName,Font.BOLD, fontSize));
        int tipsLen = tips.length();
        g.drawString(tips,(mapLenX*cellWidth-tipsLen*fontSize/2-(tipsLen-1)*space)/2,fontTop);
        String snakeLenStr = "SNAKELEN:"+len;
        tipsLen = snakeLenStr.length();
        g.drawString(snakeLenStr,(mapLenX*cellWidth-tipsLen*fontSize/2-(tipsLen-1)*space)/2,fontTop+fontSize);
    }

    /**
     * 画Snake
     * @param g
     */
    private void paintSnake(Graphics g){
        for(int i = 0;i < len; i++){
            if(i == 0){
                g.setColor(Color.red);
            }else if(i == len-1){
                g.setColor(Color.cyan);
            }else{
                g.setColor(Color.BLUE);
            }
            fill3DRect(g,snakeX[i],snakeY[i]);
        }
    }
    /**
     * 画地图
     * @param g
     */
    private void paintMap(Graphics g){
        for(int i = 0;i< mapLenX;i++){
            for(int j = 0;j<mapLenY;j++){
                g.setColor(Color.WHITE);
                fill3DRect(g,i,j);
            }
        }
    }
    /**
     * 初始化Snake位置
     */
    private void initSnake(){
        len = initLen;
        snakeDir = "R";
        for(int i = 0;i < len;i++){
            if(i == 0){
                snakeX[i] = initSnakeX;
            }else{
                snakeX[i] = snakeX[i-1] - 1;
            }
            snakeY[i] = initSnakeY;
        }
    }
    /**
     * 画食物
     */
    private void paintPoint(Graphics g){
        g.setColor(Color.MAGENTA);
        fill3DRect(g,pointX,pointY);
    }
    /**
     * 随机食物坐标
     */
    private void initPoint(){
        ArrayList<String> pointList = new ArrayList<>();
        // 为防止Snake过长时，随机n次后食物还是随机在Snake上，超过一个计时器后食物位置仍没有取出
        // 暂决定取出地图所有可以存放食物的位置，保证每次随机都能拿到
        for(int i = 0;i < mapLenX;i++){
            for(int j = 0;j < mapLenY;j++){
                boolean isSuitPoint = true;
                for(int s = 0;s<len;s++){
                    if(i == snakeX[s] && j == snakeY[s]){
                        isSuitPoint = false;
                    }
                }
                if(isSuitPoint){
                    pointList.add(i+"_"+j);
                }
            }
        }
        // 剩余空白处小于设置最小数时，游戏胜利
        if(pointList.size() > minWhitePoint){
            int pointNum = pointList.size();
            String[] pointStr = pointList.get((int)(Math.random()*pointNum)).split("_");
            pointX = Integer.valueOf(pointStr[0]);
            pointY = Integer.valueOf(pointStr[1]);
        }else{
            isRun = false;
            isWin = true;
        }
    }
    /**
     * 填充（x,y）
     * @param g
     * @param x
     * @param y
     */
    private void fill3DRect(Graphics g,int x,int y){
        g.fill3DRect(x*cellWidth,y*cellWidth,cellWidth,cellWidth,true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 监听空格键
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            isRun = !isRun;
            this.repaint();
        }
        if(isOver){
            return;
        }
        if(!isRun){
            return;
        }
        if(e.getKeyCode()== KeyEvent.VK_LEFT){
            move("L");
            this.repaint();
        }
        if(e.getKeyCode()== KeyEvent.VK_RIGHT){
            move("R");
            this.repaint();
        }
        if(e.getKeyCode()== KeyEvent.VK_UP){
            move("U");
            this.repaint();
        }
        if(e.getKeyCode()== KeyEvent.VK_DOWN){
            move("D");
            this.repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * 一个计时器后执行
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(isRun){
            autoRun();
//            move(snakeDir);
            this.repaint();
        }
    }


    /**
     * 移动
     * 判断是否可移动 左右，上下
     * 判断触碰（win,撞墙，吃自己，吃食物）
     * 更新位置
     * @param dir
     */
    private boolean move(String dir){
        // 先拿到Snake最后一节的位置，用于吃到食物时增加Snake
        int lastSnakeBodyX = snakeX[len - 1];
        int lastSnakeBodyY = snakeY[len - 1];
        int step = 1;
        if(dir.equals("R") || dir.equals("L")){
            if(dir.equals("R")){
                if(snakeDir.equals("L")){
                    return false;
                }
                if(snakeX[0] + step >= mapLenX){
                    setGameStat("GAME OVER");
                    return false;
                }
            }else{
                if(snakeDir.equals("R")){
                    return false;
                }
                step = -1;
                if(snakeX[0] + step < 0){
                    setGameStat("GAME OVER");
                    return false;
                }
            }

            for(int i = len - 1;i > 0;i--){
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
            snakeX[0] = snakeX[0] + step;
            for(int i = len - 1;i > 0;i--){
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                    setGameStat("GAME OVER");
                    return false;
                }
            }

        }else{
            if(dir.equals("U")){
                if(snakeDir.equals("D")){
                    return false;
                }
                step = -1;
                if(snakeY[0] + step < 0){
                    setGameStat("GAME OVER");
                    return false;
                }
            }else{
                if(snakeDir.equals("U")){
                    return false;
                }
                if(snakeY[0] + step >= mapLenY){
                    setGameStat("GAME OVER");
                    return false;
                }
            }

            for(int i = len - 1;i > 0;i--){
                snakeX[i] = snakeX[i - 1];
                snakeY[i] = snakeY[i - 1];
            }
            snakeY[0] = snakeY[0] + step;
            for(int i = len - 1;i > 0;i--){
                if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                    setGameStat("GAME OVER");
                    return false;
                }
            }
        }
        snakeDir = dir;
        System.out.println("当前snake方向:"+snakeDir);
        checkAndUpdatePoint(lastSnakeBodyX,lastSnakeBodyY);
        return true;
    }
    /**
     * 若吃到食物，更新snake长度和位置，更新下一食物位置
     */
    private void checkAndUpdatePoint(int lastSnakeBodyX,int lastSnakeBodyY){
        if(snakeX[0] == pointX && snakeY[0] == pointY){
            len++;
            snakeX[len - 1] = lastSnakeBodyX;
            snakeY[len - 1] = lastSnakeBodyY;
            initPoint();
        }
    }
    /**
     * 修改游戏状态
     */
    private void setGameStat(String overTips){
        isOver = true;
        isRun = false;
        this.overTips = overTips;
        snakeList.clear();
    }

    /**
     * 自动移动
     */
    private void autoRun() {
        autoV3();
    }
    /**
     * 版本1，直接向食物移动
     */
    private void autoV1(){
        // 版本1 snake直接向食物靠近
        boolean canRun = false;
        if(snakeX[0] < pointX){
            canRun = move("R");
        }else if(snakeX[0] > pointX){
            canRun = move("L");
        }else if(snakeY[0] < pointY){
            canRun = move("D");
        }else if(snakeY[0] > pointY){
            canRun = move("U");
        }
        if(!canRun){
            if(!isOver){
                // 先随机
                if(snakeDir.equals("R") || snakeDir.equals("L")){
                    if(snakeY[0] > mapLenY/2){
                        move("U");
                    }else{
                        move("D");
                    }
                }else{
                    if(snakeX[0] > mapLenX){
                        move("L");
                    }else{
                        move("R");
                    }
                }
            }
        }
    }
    // 版本2 无脑无敌排列移动
    private void autoV2(){
        if(snakeX[0] < mapLenX-1 && !snakeDir.equals("L")){
            if(snakeX[0] == mapLenX - 2 && snakeDir.equals("U")){
                if(snakeY[0] == 0){
                    move("L");
                }else{
                    move("U");
                }
            }else if(snakeY[0] < mapLenY-1 && !snakeDir.equals("U")){
                move("D");
            }else if(snakeY[0] == mapLenY-1){
                move("R");
                move("U");
            }else if(snakeY[0] > 1 && !snakeDir.equals("D")){
                move("U");
            }else if(snakeY[0] == 1){
                move("R");
                move("D");
            }
        }else if(snakeX[0] == mapLenX-1){
            if(snakeY[0] > 0){
                move("U");
            }else if(snakeY[0] == 0){
                move("L");
            }
        }else{
            if(snakeX[0] > 0){
                move("L");
            }else if(snakeX[0] == 0){
                move("D");
            }
        }
    }

    /**
     * 版本三，简单判断移动
     */
    private void autoV3(){
        /**
         *  TODO 未完成
         *
         * 1.可以找到吃食物的路线
         *      1.吃到食物后，可以找到自己的尾巴
         *          移动一步
         *      2.吃到食物后，找不到自己的尾巴
         *           沿最远路径想尾巴移动一步
         * 2. 找不到食物的路线
         *      1. 可以找到尾巴
         *          沿着最远向尾部移动一步
         *      2. 找不到尾巴
         *          按存活时间最长移动一步
         */
        // 将snake位置放到list中，用于调用bfs
        snakeList.addFirst(new Point(snakeX[0],snakeY[0]));
        for(int i = 1;i< len;i++){
            snakeList.addLast(new Point(snakeX[i],snakeY[i]));
        }
        // 路线
        LinkedList<StepInfo> dirList = sdfMinStep.bfs(snakeList,new Point(pointX,pointY));
        System.out.println("计算路线："+dirList);

        if(dirList == null){
//            setGameStat("AI AM LOST");
//            return;
            dirList = sdfMaxStep.bfs(snakeList,snakeList.getLast());
            if(dirList == null){
                setGameStat("AI AM LOST");
                return;
            }
        }
        System.out.println("计算步数："+dirList.size());
        // 先按照路线模拟走一遍，
        // 模拟snake数据
        LinkedList<Point> tempSnakeList = new LinkedList<>(snakeList);
        // 计算模拟之后的snake位置
        int stepNum = dirList.size();
        for(int i = 0;i < stepNum;i++){
            tempSnakeList.addFirst(dirList.get(i).getPoint());
            if(i != stepNum - 1){
                tempSnakeList.removeLast();
            }
        }
        System.out.println("模拟吃到食物后的snake位置:"+tempSnakeList);
        // 计算模拟之后的snake是可以走到snake尾
        LinkedList<StepInfo> tempDirList = sdfMinStep.bfs(tempSnakeList,tempSnakeList.getLast());
        System.out.println("模拟的snake到snake尾的路径："+tempDirList);

        if(tempDirList == null){
            // 向snake尾走一步 TODO 按最远走
            // 更新路线
            dirList = sdfMaxStep.bfs(snakeList,snakeList.getLast());
            System.out.println("向snake尾走一步："+dirList);
            if(dirList == null){
                setGameStat("AI AM LOST");
                return;
            }
        }
        System.out.println("模拟的snake到snake尾的路径："+tempDirList.size());
        snakeList.clear();
        System.out.println("准备路线："+dirList);
        System.out.println("执行步数："+dirList.size());
        if(dirList.size() > 0){
            System.out.println("执行方向："+dirList.getFirst());
            move(dirList.getFirst().getDir());
        }
    }
}