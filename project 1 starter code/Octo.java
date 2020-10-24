import java.util.Optional;

public class Octo extends Entity{
    public static final String OCTO_KEY = "octo";
    public static final int OCTO_NUM_PROPERTIES = 7;
    public static final int OCTO_ID = 1;
    public static final int OCTO_COL = 2;
    public static final int OCTO_ROW = 3;
    public static final int OCTO_LIMIT = 4;
    public static final int OCTO_ACTION_PERIOD = 5;
    public static final int OCTO_ANIMATION_PERIOD = 6;

    public void executeOctoFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget = world.findNearest(this.position,
                EntityKind.ATLANTIS);

        if (fullTarget.isPresent() &&
                this.moveToFull(scheduler, world, fullTarget.get()))
        {
            //at atlantis trigger animation
            fullTarget.get().scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            this.transformFull(scheduler, world, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    world.createActivityAction(this, imageStore),
                    this.actionPeriod);
        }
    }


    public void executeOctoNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> notFullTarget = world.findNearest(this.position,
                EntityKind.FISH);

        if (!notFullTarget.isPresent() ||
                !this.moveToNotFull(scheduler, world, notFullTarget.get()) ||
                !this.transformNotFull(scheduler, world, imageStore))
        {
            scheduler.scheduleEvent(this,
                    world.createActivityAction(this, imageStore),
                    this.actionPeriod);
        }
    }
}
