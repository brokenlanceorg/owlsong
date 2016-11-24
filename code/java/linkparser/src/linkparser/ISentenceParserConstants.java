package linkparser;

/**
 * 
 */
public interface ISentenceParserConstants
{

   /**
    * The problem with terminal punctuation is that the period can be used
    * in abbreviations such as intials in a proper name. The period also
    * appears in decimal numbers.
    *
    * @see ABBREVIATIONS 
    *
    *
    */
   public static final String TERMINAL_PUNCTUATION = ".?!"; // not sure about interrobang

   public static final String INTERNAL_PUNCTUATION = ",;:-_()[]{}'\"~@#$%^&*+=|";

   public static final String[] ABBREVIATIONS = { "Dr.", "M.A.", "Ph.D.", "LL.D.", "P.S.", "p.s.", "etc.", 
                                                  "i.e.", "Ms.", "Mr.", "U.S.A", "A.M.", "B.S.", "D.Litt.", "Jr.",
                                                  "A.D.", "B.C.", "e.g.", "P.M.", "Sr.", "ESQ.", "No.", "Mrs.",
                                                  "Prof.", "Gov.", "Sen.", "Rep.", "Gen.", "Brig.", "Col.", "Capt.",
                                                  "Lieut.", "Lt.", "Sgt.", "Pvt.", "Cmdr.", "Adm.", "Corp.", "St.",
                                                  "Mt.", "Ft.", "St.", "Ave.", "Av.", "Pl.", "Ct.", "Gr.",
                                                  "Rd.", "Blvd.", "Pkwy.", "Hwy.", "Assn.", "Corp.", "Co.", "Inc.",
                                                  "Pty.", "Ltd.", "Bldg.", "Rev."
                                                 };

}
