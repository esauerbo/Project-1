/*
Action: ideally what our various entities might do in our virutal world
 */

final class Action
{
   private ActionKind kind;
   public Entity entity;
   public WorldModel world;
   public ImageStore imageStore;
   private int repeatCount;

   public Action(ActionKind kind, Entity entity, WorldModel world,
      ImageStore imageStore, int repeatCount)
   {
      this.kind = kind;
      this.entity = entity;
      this.world = world;
      this.imageStore = imageStore;
      this.repeatCount = repeatCount;
   }

   public void executeAction(EventScheduler scheduler)
   {
      switch (this.kind)
      {
         case ACTIVITY:
            this.executeActivityAction(scheduler);
            break;

         case ANIMATION:
            this.executeAnimationAction(scheduler);
            break;
      }
   }

   public void executeAnimationAction(EventScheduler scheduler)
   {
      this.entity.nextImage();

      if (this.repeatCount != 1)
      {
         scheduler.scheduleEvent(this.entity,
                 entity.createAnimationAction(
                         Math.max(this.repeatCount - 1, 0)),
                 this.entity.getAnimationPeriod());
      }
   }

   public void executeActivityAction(EventScheduler scheduler)
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
