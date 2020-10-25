import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class SGrass extends Moved {
    public static final String SGRASS_KEY = "seaGrass";
    public static final int SGRASS_NUM_PROPERTIES = 5;
    public static final int SGRASS_ID = 1;
    public static final int SGRASS_COL = 2;
    public static final int SGRASS_ROW = 3;
    public static final int SGRASS_ACTION_PERIOD = 4;

    protected int resourceLimit;
    protected int resourceCount;

    public SGrass(String id, Point position,
                  List<PImage> images, int actionPeriod, int animationPeriod, int resourceLimit, int resourceCount) {
        super(id, position, images, actionPeriod, animationPeriod);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    public static SGrass createSgrass(String id, Point position, int actionPeriod,
                                      List<PImage> images)
    {
        return new SGrass(id, position, images,actionPeriod, 0, 0,0);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Point> openPt = world.findOpenAround(this.getPosition());

        if (openPt.isPresent())
        {
            Fish fish = Fish.createFish(Fish.FISH_ID_PREFIX + this.getId(),
                    openPt.get(), Fish.FISH_CORRUPT_MIN +
                            WorldView.rand.nextInt(Fish.FISH_CORRUPT_MAX - Fish.FISH_CORRUPT_MIN),
                    imageStore.getImageList(Fish.FISH_KEY));
            world.addEntity(fish);
            fish.scheduleActions(scheduler, world, imageStore);
        }

        scheduler.scheduleEvent(this,
                Activity.createActivityAction(this, world, imageStore),
                this.getActionPeriod());
    }
}
