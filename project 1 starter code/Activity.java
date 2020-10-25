public class Activity extends Action{

    private Entity entity;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public Activity( Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        super(entity, world, imageStore, repeatCount);
    }

    public static Activity createActivityAction(Entity entity, WorldModel world, ImageStore imageStore)
    {
        return new Activity(entity, world, imageStore, 0);
    }



    public void executeAction(EventScheduler scheduler)
    {
        if (this.entity instanceof OctoFull) {
            ((OctoFull) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof OctoNotFull) {
            ((OctoNotFull) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof Fish) {
            ((Fish) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof Crab) {
            ((Fish) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof Quake) {
            ((Quake) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof Atlantis) {
            ((Atlantis) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        if (this.entity instanceof SGrass) {
            ((SGrass) this.entity).executeActivity(this.world,
                    this.imageStore, scheduler);
        }
        throw new UnsupportedOperationException(
                String.format("executeActivityAction not supported for %s"));
    }


}
