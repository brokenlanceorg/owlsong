package linkparser;

import java.util.*;

/**
 *
 */
public class Document
{

   // COMMENT
   private ArrayList< Paragraph > _paragraphs = new ArrayList< Paragraph >();

   /**
    *
    */
   public void addParagraph( Paragraph para )
   {
      _paragraphs.add( para );
      ParagraphParser.parse( para );
      System.out.println( "Adding a paragraph: " + para );
   }

   /**
    *
    */
   public ArrayList< Paragraph > getParagraphs()
   {
      return _paragraphs;
   }

}
