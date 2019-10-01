package cn.xsxlq.game.pojo;

/**
 * @author wangjs6
 * @version 1.0
 * @Description:
 * @date: 2019/9/15 23:06
 */
public class StepInfo {
    String dir;
    Point point;

    public StepInfo(String dir, Point point) {
        this.dir = dir;
        this.point = point;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "StepInfo{" +
                "dir='" + dir + '\'' +
                ", point=" + point +
                '}';
    }
}
