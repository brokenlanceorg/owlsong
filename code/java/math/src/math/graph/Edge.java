package math.graph;

/**
 *
 *
 */
public class Edge implements Comparable< Edge >
{

   private int    _leftVertex;
   private int    _rightVertex;
   private double _weight;
   
   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public Edge()
   {
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public Edge( int l, int r, double w )
   {
      setLeftVertex( l );
      setRightVertex( r );
      setWeight( w );
   }

   /**
    *
    * @param int
    */
   public void setLeftVertex( int leftVertex )
   {
      _leftVertex = leftVertex;
   }

   /**
    *
    * @return int
    */
   public int getLeftVertex()
   {
      return _leftVertex;
   }

   /**
    *
    * @param int
    */
   public void setRightVertex( int rightVertex )
   {
      _rightVertex = rightVertex;
   }

   /**
    *
    * @return int
    */
   public int getRightVertex()
   {
      return _rightVertex;
   }

   /**
    *
    * @param double
    */
   public void setWeight( double weight )
   {
      _weight = weight;
   }

   /**
    *
    * @return double
    */
   public double getWeight()
   {
      return _weight;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public boolean equals( Object o )
   {
      boolean value = false;
      if( o.getClass() == KruskalMinimalSpanningTree.class )
      {
         Edge other = (Edge) o;
         value = (    getLeftVertex() == other.getLeftVertex() && getRightVertex() == other.getRightVertex()
                   || getRightVertex() == other.getLeftVertex() && getLeftVertex() == other.getRightVertex() );
      }
      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int hashCode()
   {
      int value = Integer.valueOf( getLeftVertex() ).hashCode() + Integer.valueOf( getRightVertex() ).hashCode();
      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int compareTo( Edge e )
   {
      int value = 0;

      if( getWeight() < e.getWeight() )
      {
         value = -1;
      }
      else if( getWeight() > e.getWeight() )
      {
         value = 1;
      }

      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String value = "Edge: ";
      value += "   left: " + getLeftVertex();
      value += "   right: " + getRightVertex();
      value += "   weight: " + getWeight();
      return value;
   }

}
