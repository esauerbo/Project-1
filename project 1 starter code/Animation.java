public class Animation extends Action {

    public Animation(Entity entity, WorldModel world,
                     ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }

    public void executeAction(EventScheduler scheduler)
    {
        this.getEntity().nextImage();

        if (this.getRepeatCount() != 1)
        {
            scheduler.scheduleEvent(this.getEntity(),
                    createAnimationAction(this.entity,
                            Math.max(this.getRepeatCount() - 1, 0)),
                    this.getEntity().getAnimationPeriod());
        }
    }

    public static Animation createAnimationAction(Entity entity, int repeatCount)
    {
        return new Animation(entity,null, null, repeatCount);
    }
}
