import processing.core.PImage;

import java.util.List;

public class Quake extends Entity{

    public static final String QUAKE_KEY = "quake";
    public static final String QUAKE_ID = "quake";
    public static final int QUAKE_ACTION_PERIOD = 1100;
    public static final int QUAKE_ANIMATION_PERIOD = 100;
    public static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;

    public Quake(EntityKind kind, String id, Point position,
                List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod){
        super(kind, id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }

    public static Quake createQuake(Point position, List<PImage> images)
    {
        return new Quake(EntityKind.QUAKE, Quake.QUAKE_ID, position, images,
                0, 0, Quake.QUAKE_ACTION_PERIOD, Quake.QUAKE_ANIMATION_PERIOD);
    }

    public void executeQuakeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        scheduler.unscheduleAllEvents(this);
        world.removeEntity(this);
    }
}
