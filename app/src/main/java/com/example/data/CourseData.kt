package com.example.data

data class ChapterCourse(
    val id: String,
    val classLevel: Int, // 1 to 10
    val board: String, // "CBSE" or "ICSE"
    val subject: String,
    val chapterTitle: String,
    val keyFormulas: List<String>,
    val conceptsSummary: String,
    val sampleQuestion: String,
    val youtubeVideoTitle: String,
    val youtubeSearchQuery: String,
    val isFree: Boolean = true
)

object CourseCatalog {
    val sampleChapters: List<ChapterCourse> = listOf(
        // Class 10 - CBSE Mathematics
        ChapterCourse(
            id = "cbse_10_math_quad",
            classLevel = 10,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Quadratic Equations",
            keyFormulas = listOf(
                "Standard Form: ax² + bx + c = 0 (a ≠ 0)",
                "Discriminant D = b² - 4ac",
                "Quadratic Formula: x = (-b ± √D) / (2a)",
                "Nature of Roots: D > 0 (two distinct real roots), D = 0 (equal real roots), D < 0 (no real roots)"
            ),
            conceptsSummary = "Factorisation method, completing the square, discriminant method, and real-life word problems involving speed, work, and geometry.",
            sampleQuestion = "Find the roots of 2x² - 7x + 3 = 0 using the quadratic formula.",
            youtubeVideoTitle = "Class 10 Quadratic Equations Full Chapter One Shot (CBSE)",
            youtubeSearchQuery = "Class 10 CBSE Quadratic Equations One Shot Gurudev Deepak Bhardwaj"
        ),
        // Class 10 - CBSE Science
        ChapterCourse(
            id = "cbse_10_sci_light",
            classLevel = 10,
            board = "CBSE",
            subject = "Science (Physics)",
            chapterTitle = "Light: Reflection & Refraction",
            keyFormulas = listOf(
                "Mirror Formula: 1/f = 1/v + 1/u",
                "Magnification (Mirror): m = -v/u = h'/h",
                "Snell's Law: n = sin(i) / sin(r)",
                "Lens Formula: 1/f = 1/v - 1/u",
                "Power of Lens: P = 1 / f (in metres), Unit: Dioptre (D)"
            ),
            conceptsSummary = "Laws of reflection, spherical mirrors (concave/convex), sign convention, refraction through glass slab, lens ray diagrams, and refractive index.",
            sampleQuestion = "A concave mirror produces three times magnified real image of an object placed at 10 cm in front of it. Where is the image located?",
            youtubeVideoTitle = "Light Reflection and Refraction Class 10 CBSE Science Full Chapter",
            youtubeSearchQuery = "Light Reflection and Refraction Class 10 CBSE One Shot"
        ),
        // Class 10 - ICSE Mathematics
        ChapterCourse(
            id = "icse_10_math_gst",
            classLevel = 10,
            board = "ICSE",
            subject = "Mathematics",
            chapterTitle = "Goods and Services Tax (GST)",
            keyFormulas = listOf(
                "Intra-State: CGST = SGST = (GST Rate / 2)",
                "Inter-State: IGST = Full GST Rate",
                "Input Tax Credit (ITC) = Tax on Purchase",
                "Net GST Payable = Output GST - Input Tax Credit"
            ),
            conceptsSummary = "Computing CGST, SGST, IGST on sales at wholesale, dealer, and consumer tiers under ICSE syllabus.",
            sampleQuestion = "A dealer buys an article for ₹6,000 and sells it to a consumer at 10% profit. If the GST rate is 18%, calculate the CGST and SGST paid by the consumer.",
            youtubeVideoTitle = "ICSE Class 10 Commercial Mathematics GST Complete Explanation",
            youtubeSearchQuery = "ICSE Class 10 GST Full Chapter Explanation"
        ),
        // Class 9 - CBSE Science
        ChapterCourse(
            id = "cbse_9_sci_motion",
            classLevel = 9,
            board = "CBSE",
            subject = "Science (Physics)",
            chapterTitle = "Motion & Laws of Motion",
            keyFormulas = listOf(
                "v = u + at",
                "s = ut + ½ at²",
                "v² - u² = 2as",
                "Newton's 2nd Law: F = m × a",
                "Momentum p = m × v, Conservation: m₁u₁ + m₂u₂ = m₁v₁ + m₂v₂"
            ),
            conceptsSummary = "Distance vs Displacement, Uniform & Non-Uniform motion, velocity-time graphs, inertia, Newton's 3 laws.",
            sampleQuestion = "A car accelerates uniformly from 18 km/h to 36 km/h in 5 seconds. Calculate its acceleration and the distance covered.",
            youtubeVideoTitle = "Class 9 Physics Motion Equations and Numericals One Shot",
            youtubeSearchQuery = "Class 9 Physics Motion Equations One Shot"
        ),
        // Class 8 - Mathematics
        ChapterCourse(
            id = "cbse_8_math_linear",
            classLevel = 8,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Linear Equations in One Variable",
            keyFormulas = listOf(
                "ax + b = c ⇒ x = (c - b) / a",
                "Transposition method for balancing equations",
                "Cross-Multiplication for (ax + b)/(cx + d) = m/n"
            ),
            conceptsSummary = "Solving linear equations with variables on one side and both sides, age problems, digit interchange problems.",
            sampleQuestion = "The ages of Rahul and Haroon are in the ratio 5:7. Four years later the sum of their ages will be 56 years. What are their present ages?",
            youtubeVideoTitle = "Class 8 Maths Linear Equations in One Variable Full Chapter",
            youtubeSearchQuery = "Class 8 Maths Linear Equations in One Variable Full Chapter"
        ),
        // Class 7 - Science
        ChapterCourse(
            id = "cbse_7_sci_heat",
            classLevel = 7,
            board = "CBSE",
            subject = "Science",
            chapterTitle = "Heat and Temperature",
            keyFormulas = listOf(
                "°C to °F Conversion: F = (C × 9/5) + 32",
                "Conduction (Solids), Convection (Liquids/Gases), Radiation (Vacuums)"
            ),
            conceptsSummary = "Clinical vs Laboratory thermometer, modes of heat transfer, sea breeze and land breeze, woollen clothes insulation.",
            sampleQuestion = "Why are the handles of cooking utensils made of plastic or wood?",
            youtubeVideoTitle = "Class 7 Science Chapter 4 Heat Full Chapter Explanation",
            youtubeSearchQuery = "Class 7 Science Heat Full Chapter Explanation"
        ),
        // Class 6 - Mathematics
        ChapterCourse(
            id = "cbse_6_math_integers",
            classLevel = 6,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Integers & Number Line",
            keyFormulas = listOf(
                "(+) + (+) = (+)",
                "(-) + (-) = (-)",
                "(+) × (-) = (-)",
                "Absolute value |a| ≥ 0"
            ),
            conceptsSummary = "Representation of positive and negative numbers on number line, addition and subtraction rules, real-world elevations & temperatures.",
            sampleQuestion = "Find the sum of (-13) + (+18) + (-5) + (-2).",
            youtubeVideoTitle = "Class 6 Maths Integers Complete Chapter with Examples",
            youtubeSearchQuery = "Class 6 Maths Integers Complete Chapter"
        ),
        // Class 5 - EVS & Maths
        ChapterCourse(
            id = "cbse_5_math_shapes",
            classLevel = 5,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Shapes, Angles & Fractions",
            keyFormulas = listOf(
                "Right Angle = 90°",
                "Acute Angle < 90°, Obtuse Angle > 90°",
                "Equivalent Fractions: a/b = (a×k)/(b×k)"
            ),
            conceptsSummary = "Identifying shapes, clock hands angles, numerator and denominator, adding like fractions.",
            sampleQuestion = "What type of angle is formed by the hands of a clock at 3:00?",
            youtubeVideoTitle = "Class 5 Maths Shapes and Angles Animation Lesson",
            youtubeSearchQuery = "Class 5 Maths Shapes and Angles Animation Lesson"
        ),
        // Class 4 - Mathematics
        ChapterCourse(
            id = "cbse_4_math_tables",
            classLevel = 4,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Multiplication & Division Fun",
            keyFormulas = listOf(
                "Dividend = (Divisor × Quotient) + Remainder",
                "Multiplication is Repeated Addition"
            ),
            conceptsSummary = "Multiplication tricks, word problems, sharing items equally, remainders.",
            sampleQuestion = "A box has 8 packets of pencils. Each packet has 12 pencils. How many pencils are in the box?",
            youtubeVideoTitle = "Class 4 Maths Multiplication and Division Easy Tricks",
            youtubeSearchQuery = "Class 4 Maths Multiplication and Division"
        ),
        // Class 1 to 3 - Foundational Learning
        ChapterCourse(
            id = "cbse_2_math_numbers",
            classLevel = 2,
            board = "CBSE",
            subject = "Mathematics",
            chapterTitle = "Counting, Place Value & Addition",
            keyFormulas = listOf(
                "Tens and Ones (e.g. 24 = 2 Tens + 4 Ones)",
                "Skip Counting by 2, 5, 10"
            ),
            conceptsSummary = "Number names, ascending & descending order, simple two-digit addition with carryover.",
            sampleQuestion = "What comes between 49 and 51? Write its number name.",
            youtubeVideoTitle = "Class 2 Maths Place Value and Addition Fun Video for Kids",
            youtubeSearchQuery = "Class 2 Maths Place Value and Addition"
        )
    )

    fun getChaptersForClass(classLevel: Int, board: String): List<ChapterCourse> {
        val matches = sampleChapters.filter { it.classLevel == classLevel && (it.board == board || it.board == "CBSE") }
        return if (matches.isNotEmpty()) matches else sampleChapters.filter { it.classLevel == classLevel }
    }
}
