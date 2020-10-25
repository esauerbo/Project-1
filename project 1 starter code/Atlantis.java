import processing.core.PImage;

import java.util.List;

public class Atlantis extends Moved{

    public static final String ATLANTIS_KEY = "atlantis";
    public static final int ATLANTIS_NUM_PROPERTIES = 4;
    public static final int ATLANTIS_ID = 1;
    public static final int ATLANTIS_COL = 2;
    public static final int ATLANTIS_ROW = 3;
    public static final int ATLANTIS_ANIMATION_PERIOD = 70;
    public static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

    protected int resourceLimit;
    protected int resourceCount;

    public Atlantis(String id, Point position,
                    List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }


    public static Atlantis createAtlantis(String id, Point position,
                                   List<PImage> images){
        return new Atlantis(id, position, images,
                0, 0, 0, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
