package math.graph;

/**
 *
 *
 */
public class Vertex implements Comparable< Vertex >
{

   private int    _vertexID;
   private int    _degree;
   private int    _color;
   
   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public Vertex()
   {
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public Vertex( int i, int d )
   {
      setVertexID( i );
      setDegree( d );
   }

   /**
    *
    * @param int
    */
   public void setVertexID( int id )
   {
      _vertexID = id;
   }

   /**
    *
    * @return int
    */
   public int getVertexID()
   {
      return _vertexID;
   }

   /**
    *
    * @param int
    */
   public void setDegree( int degree )
   {
      _degree = degree;
   }

   /**
    *
    * @return int
    */
   public int getDegree()
   {
      return _degree;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public boolean equals( Object o )
   {
      boolean value = false;
      if( o.getClass() == Vertex.class )
      {
         Vertex other = (Vertex) o;
         value = ( getVertexID() == other.getVertexID() && getDegree() == other.getDegree() );
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
      int value = Integer.valueOf( getVertexID() ).hashCode() + Integer.valueOf( getDegree() ).hashCode();
      return value;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public int compareTo( Vertex e )
   {
      int value = 0;

      if( getDegree() < e.getDegree() )
      {
         value = -1;
      }
      else if( getDegree() > e.getDegree() )
      {
         value = 1;
      }

      return value;
   }

   /**
    *
    * @param int
    */
   public void setColor( int color )
   {
      _color = color;
   }

   /**
    *
    * @return int
    */
   public int getColor()
   {
      return _color;
   }

   /**
    *
    * @param TYPE
    * @return TYPE
    */
   public String toString()
   {
      String value = "\nVertex:\n";
      value += "   ID:     " + getVertexID() + "\n";
      value += "   degree: " + getDegree() + "\n";
      value += "   color:  " + getColor() + "\n";
      return value;
   }

}
