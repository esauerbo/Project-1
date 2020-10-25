import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public abstract class Octo extends Moved {
    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;

    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    protected int resourceLimit;
    protected int resourceCount;

    /*-----------Getters-----------*/

    public int getResourceLimit() { return resourceLimit; }
    public int getResourceCount() { return resourceCount; }


    public Octo(String id, Point position, List<PImage> images, int actionPeriod,
                int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }

}
