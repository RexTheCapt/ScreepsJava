package def.screeps;

import jsweet.lang.Optional;

@jsweet.lang.Interface
public class LineStyle extends jsweet.lang.Object{
    @Optional
    public double width;
    @Optional
    public String color;
    @Optional
    public double opacity;
    @Optional
    public String lineStyle;
}
