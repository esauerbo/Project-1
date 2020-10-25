import processing.core.PImage;

import java.util.List;

public class Fish extends Moved {
    public static final String FISH_KEY = "fish";
    public static final int FISH_NUM_PROPERTIES = 5;
    public static final int FISH_ID = 1;
    public static final int FISH_COL = 2;
    public static final int FISH_ROW = 3;
    public static final int FISH_ACTION_PERIOD = 4;

    public static final String FISH_ID_PREFIX = "fish -- ";
    public static final int FISH_CORRUPT_MIN = 20000;
    public static final int FISH_CORRUPT_MAX = 30000;
    public static final int FISH_REACH = 1;

    protected int resourceLimit;
    protected int resourceCount;


    public Fish(String id, Point position, List<PImage> images, int actionPeriod,
                     int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
    }


    public static Fish createFish(String id, Point position, int actionPeriod,
                                  List<PImage> images){
        return new Fish(id, position, images, actionPeriod, 0,
                0, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Point pos = this.getPosition();  // store current position before removing

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        Crab crab = Crab.createCrab(this.getId() + Crab.CRAB_ID_SUFFIX,
                pos, this.getActionPeriod() / Crab.CRAB_PERIOD_SCALE,
                Crab.CRAB_ANIMATION_MIN +
                        WorldView.rand.nextInt(Crab.CRAB_ANIMATION_MAX - Crab.CRAB_ANIMATION_MIN),
                imageStore.getImageList(Crab.CRAB_KEY));

        world.addEntity(crab);
        crab.scheduleActions(scheduler, world, imageStore);
    }
}
