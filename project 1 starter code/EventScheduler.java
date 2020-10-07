import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{
   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   private PriorityQueue<Event> eventQueue;
   private Map<Entity, List<Event>> pendingEvents;
   private double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleActions(Entity entity, WorldModel world, ImageStore imageStore)
   {
      switch (entity.kind)
      {
         case OCTO_FULL:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            this.scheduleEvent(entity, entity.createAnimationAction(0),
                    entity.getAnimationPeriod());
            break;

         case OCTO_NOT_FULL:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            this.scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
            break;

         case FISH:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            break;

         case CRAB:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            this.scheduleEvent(entity,
                    entity.createAnimationAction(0), entity.getAnimationPeriod());
            break;

         case QUAKE:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            this.scheduleEvent(entity,
                    entity.createAnimationAction(entity.QUAKE_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         case SGRASS:
            this.scheduleEvent(entity,
                    world.createActivityAction(entity, imageStore),
                    entity.actionPeriod);
            break;
         case ATLANTIS:
            this.scheduleEvent(entity,
                    entity.createAnimationAction(ATLANTIS_ANIMATION_REPEAT_COUNT),
                    entity.getAnimationPeriod());
            break;

         default:
      }
   }

   public boolean transformNotFull(Entity entity, WorldModel world, ImageStore imageStore)
   {
      if (entity.resourceCount >= entity.resourceLimit)
      {
         Entity octo = WorldView.createOctoFull(entity.id, entity.resourceLimit,
                 entity.position, entity.actionPeriod, entity.animationPeriod,
                 entity.images);

         world.removeEntity(entity);
         this.unscheduleAllEvents(entity);

         world.addEntity(octo);
         this.scheduleActions(octo, world, imageStore);

         return true;
      }

      return false;
   }

   public void transformFull(Entity entity, WorldModel world, ImageStore imageStore)
   {
      Entity octo = WorldView.createOctoNotFull(entity.id, entity.resourceLimit,
              entity.position, entity.actionPeriod, entity.animationPeriod,
              entity.images);

      world.removeEntity(entity);
      this.unscheduleAllEvents(entity);

      world.addEntity(octo);
      this.scheduleActions(octo, world, imageStore);
   }

   public boolean moveToNotFull(Entity octo, WorldModel world, Entity target)
   {
      if (octo.position.adjacent(target.position))
      {
         octo.resourceCount += 1;
         world.removeEntity(target);
         this.unscheduleAllEvents(target);

         return true;
      }
      else
      {
         Point nextPos = octo.nextPositionOcto(world, target.position);

         if (!octo.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               this.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public boolean moveToFull(Entity octo, WorldModel world, Entity target)
   {
      if (octo.position.adjacent(target.position))
      {
         return true;
      }
      else
      {
         Point nextPos = octo.nextPositionOcto(world, target.position);

         if (!octo.position.equals(nextPos))
         {
            Optional<Entity> occupant = world.getOccupant(nextPos);
            if (occupant.isPresent())
            {
               this.unscheduleAllEvents(occupant.get());
            }

            world.moveEntity(octo, nextPos);
         }
         return false;
      }
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * this.timeScale);
      Event event = new Event(action, time, entity);

      this.eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = this.pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      this.pendingEvents.put(entity, pending);
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = this.pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            this.eventQueue.remove(event);
         }
      }
   }

   public void removePendingEvent(Event event)
   {
      List<Event> pending = this.pendingEvents.get(event.entity);

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime(long time)
   {
      while (!this.eventQueue.isEmpty() &&
              this.eventQueue.peek().time < time)
      {
         Event next = this.eventQueue.poll();

         this.removePendingEvent(next);

         next.action.executeAction(this);
      }
   }

}
