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
        switch (this.entity.getKind())
        {
            case OCTO_FULL:
                ((Octo)this.entity).executeOctoFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case OCTO_NOT_FULL:
                ((Octo)this.entity).executeOctoNotFullActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case FISH:
                ((Fish)this.entity).executeFishActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case CRAB:
                ((Crab)this.entity).executeCrabActivity(this.world,
                        this.imageStore, scheduler);
                break;

            case QUAKE:
                ((Quake)this.entity).executeQuakeActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case SGRASS:
                ((SGrass)this.entity).executeSgrassActivity(this.world, this.imageStore,
                        scheduler);
                break;

            case ATLANTIS:
                ((Atlantis)this.entity).executeAtlantisActivity(this.world, this.imageStore,
                        scheduler);
                break;

            default:
                throw new UnsupportedOperationException(
                        String.format("executeActivityAction not supported for %s",
                                this.entity.getKind()));
        }
    }
}
