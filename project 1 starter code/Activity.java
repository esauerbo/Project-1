public class Activity extends Action {


    public Activity(Entity entity, WorldModel world,
                     ImageStore imageStore, int repeatCount){
        super(entity, world, imageStore, repeatCount);
    }


    public static Action createActivityAction(Entity entity, ImageStore imageStore, WorldModel world)
    {
        return new Activity(entity, world, imageStore, 0);
    }

    public void executeAction(EventScheduler scheduler)
    {

            if (this.entity instanceof OctoFull) {
                ((OctoFull)this.entity).executeOctoFullActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof OctoNotFull) {
                ((OctoNotFull)this.entity).executeOctoNotFullActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof Fish) {
                ((Fish)this.entity).executeFishActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof Crab) {
                ((Crab)this.entity).executeCrabActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof Quake) {
                ((Quake)this.entity).executeQuakeActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof SGrass) {
                ((SGrass)this.entity).executeSgrassActivity(this.world,
                        this.imageStore, scheduler);
            }
            if (this.entity instanceof Atlantis) {
                ((Atlantis)this.entity).executeAtlantisActivity(this.world,
                        this.imageStore, scheduler);
            } else {
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                this.entity.getClass()));
            }
    }
}
