package cn.xsxlq.game.pojo;

/**
 * @author wangjs6
 * @version 1.0
 * @Description:
 * @date: 2019/9/15 20:18
 */
public class PointDto {
    Point point;
    PointDto parent;
    int step;
    String dir;



    public PointDto(Point point, PointDto parent, int step) {
        this.point = point;
        this.parent = parent;
        this.step = step;
    }

    // 方便处理
    public PointDto(int x,int y,int step, String dir) {
        this.point = new Point(x,y);
        this.step = step;
        this.dir = dir;
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

    public PointDto getParent() {
        return parent;
    }

    public void setParent(PointDto parent) {
        this.parent = parent;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "PointDto{" +
                "point=" + point +
                ", parent=" + parent +
                ", step=" + step +
                ", dir='" + dir + '\'' +
                '}';
    }
}
