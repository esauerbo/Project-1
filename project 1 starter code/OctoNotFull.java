import java.util.Optional;

public class OctoNotFull extends Octo {

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
