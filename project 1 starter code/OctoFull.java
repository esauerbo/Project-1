public class OctoFull extends Octo {

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
}
