import java.util.Optional;

public abstract class Octo extends Entity {
    private static final String OCTO_KEY = "octo";
    private static final int OCTO_NUM_PROPERTIES = 7;
    private static final int OCTO_ID = 1;
    private static final int OCTO_COL = 2;

    private static final int OCTO_ROW = 3;
    private static final int OCTO_LIMIT = 4;
    private static final int OCTO_ACTION_PERIOD = 5;
    private static final int OCTO_ANIMATION_PERIOD = 6;

    /*-----------Getters-----------*/
    public String getOctoKey(){ return OCTO_KEY; }
    public int getOctoNumProperties(){ return OCTO_NUM_PROPERTIES; }
    public int getOctoId(){ return OCTO_ID; }
    public int getOctoCol(){ return OCTO_COL; }
    public int getOctoRow(){ return OCTO_ROW; }
    public int getOctoLimit(){ return OCTO_LIMIT; }
    public int getOctoActionPeriod(){ return OCTO_ACTION_PERIOD; }
    public int getOctoAnimationPeriod(){ return OCTO_ANIMATION_PERIOD; }

    public Octo(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, resourceCount);
    }

    public Point nextPositionOcto(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - this.position.x);
        Point newPos = new Point(this.position.x + horiz,
                this.position.y);

        if (horiz == 0 || world.isOccupied(newPos))
        {
            int vert = Integer.signum(destPos.y - this.position.y);
            newPos = new Point(this.position.x,
                    this.position.y + vert);

            if (vert == 0 || world.isOccupied(newPos))
            {
                newPos = this.position;
            }
        }

        return newPos;
    }




}
