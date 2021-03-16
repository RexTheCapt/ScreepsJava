package def.screeps;

import jsweet.lang.Optional;

@jsweet.lang.Interface
public class TextStyle extends jsweet.lang.Object{
    @Optional
    public String color;
    @Optional
    public String font;
    @Optional
    public String stroke;
    @Optional
    public String strokeWidth;
    @Optional
    public String backgroundColor;
    @Optional
    public double backgroundPadding;
    @Optional
    public String align;
    @Optional
    public double opacity;
}

