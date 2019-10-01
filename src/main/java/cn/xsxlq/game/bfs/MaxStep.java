package cn.xsxlq.game.bfs;

import cn.xsxlq.game.pojo.Point;
import cn.xsxlq.game.pojo.PointDto;
import cn.xsxlq.game.pojo.StepInfo;

import java.util.LinkedList;
import java.util.List;

/**
 * @author wangjs6
 * @version 1.0
 * @Description:
 * @date: 2019/9/15 22:45
 */
public class MaxStep {
    private int mapWidth = 50;
    private int mapHeight = 40;

    /**
     * 最大路径
     * @param snakeList snake
     * @param targetPoint 目标点
     * @return 路径
     */

    // 由于不想修改snake.move方法
    // 预返回一条带方向的list，便于调用
    public LinkedList<StepInfo> bfs(LinkedList<Point> snakeList, Point targetPoint){
        if(snakeList.size() < 1){
            return null;
        }
//        snakeList.removeLast();
        Point startPoint = snakeList.getFirst();
        if(isTarget(startPoint,targetPoint)){
            return null;
        }
        // 用于存储到达目标时的信息
        PointDto tempTargetPoint = null;
        int step = 0;
        LinkedList<PointDto> allList = new LinkedList<>();
        LinkedList<PointDto> parentList = new LinkedList<>();
        LinkedList<PointDto> childList = new LinkedList<>();
        PointDto pointDto = new PointDto(startPoint,null,0);

        allList.add(pointDto);
        parentList.add(pointDto);
        childList.add(pointDto);
        while (!parentList.isEmpty()){
            step ++;
            parentList.clear();
            parentList.addAll(childList);
            childList.clear();
            A:for (PointDto p:parentList) {
                // 1. 边界，终点，
                // 2. 已存在
                Point thisPoint = p.getPoint();
                // 上 下 左 右
                PointDto pointU = new PointDto(thisPoint.getX(),thisPoint.getY() - 1,step,"U");
                PointDto pointD = new PointDto(thisPoint.getX(),thisPoint.getY() + 1,step,"D");
                PointDto pointL = new PointDto(thisPoint.getX() - 1,thisPoint.getY(),step,"L");
                PointDto pointR = new PointDto(thisPoint.getX() + 1,thisPoint.getY(),step,"R");
                List<PointDto> udlrPointList = new LinkedList<>();
                udlrPointList.add(pointD);
                udlrPointList.add(pointL);
                udlrPointList.add(pointR);
                udlrPointList.add(pointU);
                for (PointDto pointUDLR:udlrPointList) {
                    if(isSuitPoint(pointUDLR,allList,snakeList)){
                        pointUDLR.setParent(p);
                        childList.add(pointUDLR);
                        allList.add(pointUDLR);
                        if(isTarget(pointUDLR.getPoint(),targetPoint)){
                            tempTargetPoint = pointUDLR;
                            allList.removeLast();
                            childList.removeLast();
                        }
                    }
                }
            }
        }
        System.out.println("sanke长度："+snakeList.size());
        System.out.println("snake位置："+snakeList);
        if(allList.size() < 1){
            return null;
        }
        if(tempTargetPoint == null){
            return null;
        }
        System.out.println("计算所有位置："+allList.getLast());
        System.out.println(tempTargetPoint);
        return getDirList(tempTargetPoint);
    }

    /**
     * 方向路线
     */
    private LinkedList<StepInfo> getDirList(PointDto pointDto){
        if(pointDto.getParent() == null){
            return null;
        }
        LinkedList<StepInfo> dirList = new LinkedList<>();
        while(pointDto.getParent() != null){
            dirList.addFirst(new StepInfo(pointDto.getDir(),pointDto.getPoint()));
            pointDto = pointDto.getParent();
        }
        return dirList;
    }
    /**
     * 是否终点
     * @return
     */
    private boolean isTarget(Point point,Point targetPoint){
        if(point.equals(targetPoint)){
            return true;
        }
        return false;
    }

    /**
     * 判断是否节点合适
     * @param list
     * @return
     */
    private boolean isSuitPoint(PointDto PointUDLR,List<PointDto> list,LinkedList<Point> snakeList) {
        // 界限内
        Point point = PointUDLR.getPoint();
        if(0 <= point.getX() && point.getX() < mapWidth && 0 <= point.getY() && point.getY() < mapHeight){
            // 阻碍物
            for(Point snakePoint : snakeList){
                if(snakePoint.equals(point)){
                    return false;
                }
            }
            // 没有被统计过
            for(PointDto pointDto:list){
                if(pointDto.getPoint().equals(point)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
