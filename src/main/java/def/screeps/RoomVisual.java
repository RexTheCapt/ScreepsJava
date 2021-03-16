package def.screeps;

import jsweet.lang.Ambient;

@Ambient
public class RoomVisual extends jsweet.lang.Object{
    public RoomVisual(String roomName){}
    public RoomVisual(){}

    native public void line(double x1,double  y1,double  x2,double  y2);
    native public void circle(double x, double y);
    native public void rect(double x, double y, double width, double height);
    native public void text(String text, double x, double y);

    native public void line(double x1,double  y1,double  x2,double  y2,LineStyle style);
    native public void circle(double x, double y,VisualStyle style);
    native public void rect(double x, double y, double width, double height,VisualStyle style);
    native public void text(String text, double x, double y,TextStyle style);

    native public void clear();
    native public double getSize();
    native public String export();
    native public void poly(double[][] points,VisualStyle style);

}
