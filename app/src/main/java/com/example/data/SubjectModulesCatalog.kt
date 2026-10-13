package com.example.data

object SubjectModulesCatalog {

    val allModules: List<TopicModule> = listOf(
        // ==================== CLASS 10 MATHEMATICS ====================
        TopicModule(
            id = "m10_math_quad",
            classLevel = 10,
            subject = "Mathematics",
            board = "CBSE",
            topicTitle = "Quadratic Equations",
            chapterDescription = "Standard quadratic equation ax² + bx + c = 0, Discriminant nature of roots, and Quadratic formula method.",
            formulas = listOf(
                "Standard Form: ax² + bx + c = 0 (a ≠ 0)",
                "Discriminant D = b² - 4ac",
                "Quadratic Formula: x = (-b ± √D) / (2a)",
                "If D > 0: 2 distinct real roots; If D = 0: 2 equal real roots; If D < 0: no real roots"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_m10_q1",
                    title = "Solve by Factorisation",
                    problemStatement = "Find the roots of x² - 3x - 10 = 0.",
                    difficulty = "Easy",
                    formulaUsed = "Splitting middle term: find p, q such that p+q = -3 and p*q = -10",
                    stepByStepSolution = listOf(
                        "Step 1: Write x² - 5x + 2x - 10 = 0.",
                        "Step 2: Factor out common terms: x(x - 5) + 2(x - 5) = 0.",
                        "Step 3: (x - 5)(x + 2) = 0.",
                        "Step 4: Either x - 5 = 0 => x = 5, or x + 2 = 0 => x = -2."
                    ),
                    finalAnswer = "x = 5 and x = -2",
                    hint = "Look for two numbers whose product is -10 and sum is -3: -5 and +2.",
                    youtubeVideoQuery = "Quadratic Equations Factorisation Class 10 CBSE Gurudev Deepak Bhardwaj"
                ),
                PracticeProblem(
                    id = "p_m10_q2",
                    title = "Find Nature of Roots & Solve",
                    problemStatement = "Determine the nature of roots for 2x² - 6x + 3 = 0 and solve using the quadratic formula.",
                    difficulty = "Medium",
                    formulaUsed = "D = b² - 4ac, x = (-b ± √D) / (2a)",
                    stepByStepSolution = listOf(
                        "Step 1: Identify a = 2, b = -6, c = 3.",
                        "Step 2: D = (-6)² - 4(2)(3) = 36 - 24 = 12.",
                        "Step 3: Since D = 12 > 0, there are two distinct real roots.",
                        "Step 4: x = [-(-6) ± √12] / (2 × 2) = (6 ± 2√3) / 4.",
                        "Step 5: Simplify: x = (3 ± √3) / 2."
                    ),
                    finalAnswer = "x = (3 + √3)/2 and x = (3 - √3)/2",
                    hint = "Express √12 as 2√3 before cancelling with the denominator 4.",
                    youtubeVideoQuery = "Quadratic Formula Discriminant Class 10 NCERT"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_m10_1",
                    question = "If the discriminant of ax² + bx + c = 0 is zero, the roots are:",
                    options = listOf("Real and unequal", "Real and equal", "Imaginary", "None of these"),
                    correctIndex = 1,
                    explanation = "When D = b² - 4ac = 0, both roots equal -b / (2a)."
                ),
                QuizQuestion(
                    id = "qz_m10_2",
                    question = "For the equation 3x² - 5x + 2 = 0, the value of the discriminant D is:",
                    options = listOf("1", "49", "-1", "25"),
                    correctIndex = 0,
                    explanation = "D = (-5)² - 4(3)(2) = 25 - 24 = 1."
                ),
                QuizQuestion(
                    id = "qz_m10_3",
                    question = "A quadratic equation always has at most how many real roots?",
                    options = listOf("1", "2", "3", "Infinite"),
                    correctIndex = 1,
                    explanation = "A polynomial of degree 2 can have at most 2 roots."
                )
            ),
            youtubeVideoTitle = "Class 10 Quadratic Equations Full Chapter One Shot",
            youtubeSearchQuery = "Class 10 Quadratic Equations Full Chapter One Shot CBSE"
        ),

        // ==================== CLASS 10 PHYSICS ====================
        TopicModule(
            id = "m10_phy_light",
            classLevel = 10,
            subject = "Physics",
            board = "CBSE",
            topicTitle = "Light: Reflection & Refraction",
            chapterDescription = "Spherical mirrors, mirror formula, refractive index, Snell's law, and lens formula with sign conventions.",
            formulas = listOf(
                "Mirror Formula: 1/f = 1/v + 1/u",
                "Magnification (Mirror): m = -v/u = h'/h",
                "Snell's Law: n = sin(i) / sin(r)",
                "Lens Formula: 1/f = 1/v - 1/u",
                "Power of a Lens: P = 1 / f (in metres), SI Unit: Dioptre (D)"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_phy10_1",
                    title = "Mirror Formula Problem",
                    problemStatement = "A convex mirror used for rear-view has a radius of curvature of 3.00 m. If a bus is located at 5.00 m from this mirror, find the position and nature of the image.",
                    difficulty = "Medium",
                    formulaUsed = "f = R/2, 1/f = 1/v + 1/u, m = -v/u",
                    stepByStepSolution = listOf(
                        "Step 1: Radius of curvature R = +3.00 m => Focal length f = +1.50 m.",
                        "Step 2: Object distance u = -5.00 m (by sign convention).",
                        "Step 3: 1/v = 1/f - 1/u = 1/(1.50) - 1/(-5.00) = 1/1.5 + 1/5 = 10/15 + 3/15 = 13/15.",
                        "Step 4: v = 15/13 = +1.15 m.",
                        "Step 5: Magnification m = -v/u = -(+1.15) / (-5.00) = +0.23 (Virtual, erect, diminished)."
                    ),
                    finalAnswer = "v = +1.15 m behind the mirror; Virtual and erect",
                    hint = "For a convex mirror, focal length f is always positive and object distance u is negative.",
                    youtubeVideoQuery = "Class 10 Convex Mirror Numerical NCERT Example"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_phy10_1",
                    question = "The focal length of a concave mirror having radius of curvature 30 cm is:",
                    options = listOf("+30 cm", "-15 cm", "+15 cm", "-60 cm"),
                    correctIndex = 1,
                    explanation = "For a concave mirror, f = -R/2 = -30/2 = -15 cm."
                ),
                QuizQuestion(
                    id = "qz_phy10_2",
                    question = "The SI unit of power of a lens is:",
                    options = listOf("Metre", "Watt", "Dioptre", "Lumen"),
                    correctIndex = 2,
                    explanation = "Power P = 1/f (in meters), measured in Dioptres (D)."
                ),
                QuizQuestion(
                    id = "qz_phy10_3",
                    question = "When light enters from air into glass, its speed:",
                    options = listOf("Increases", "Decreases", "Remains same", "Becomes zero"),
                    correctIndex = 1,
                    explanation = "Glass is optically denser than air, so light slows down."
                )
            ),
            youtubeVideoTitle = "Light Reflection and Refraction Class 10 Full Chapter",
            youtubeSearchQuery = "Light Reflection and Refraction Class 10 Full Chapter CBSE"
        ),

        // ==================== CLASS 10 CHEMISTRY ====================
        TopicModule(
            id = "m10_chem_reactions",
            classLevel = 10,
            subject = "Chemistry",
            board = "CBSE",
            topicTitle = "Chemical Reactions & Equations",
            chapterDescription = "Writing & balancing chemical equations, types of reactions (combination, decomposition, displacement, double displacement, redox).",
            formulas = listOf(
                "Combination: A + B -> AB",
                "Decomposition: AB -> A + B (Thermal, Electrolytic, Photolytic)",
                "Displacement: A + BC -> AC + B (More reactive metal displaces less reactive)",
                "Redox: Oxidation is Gain of Oxygen / Loss of H; Reduction is Gain of H / Loss of Oxygen"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_ch10_1",
                    title = "Balancing Chemical Equations",
                    problemStatement = "Balance the equation: Fe + H₂O -> Fe₃O₄ + H₂.",
                    difficulty = "Medium",
                    formulaUsed = "Law of Conservation of Mass: number of atoms must match on both sides",
                    stepByStepSolution = listOf(
                        "Step 1: Balance Fe: 3 atoms of Fe on RHS, so write 3Fe on LHS.",
                        "Step 2: Balance O: 4 atoms of O on RHS (Fe₃O₄), so write 4H₂O on LHS.",
                        "Step 3: Balance H: 4H₂O gives 8 H atoms on LHS, so write 4H₂ on RHS.",
                        "Step 4: Check: 3 Fe, 8 H, 4 O on both sides."
                    ),
                    finalAnswer = "3Fe + 4H₂O -> Fe₃O₄ + 4H₂",
                    hint = "Balance oxygen first using water coefficients, then balance hydrogen.",
                    youtubeVideoQuery = "How to balance Fe plus H2O equation Class 10 Chemistry"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_ch10_1",
                    question = "The reaction CaO + H₂O -> Ca(OH)₂ + Heat is an example of:",
                    options = listOf("Decomposition and Endothermic", "Combination and Exothermic", "Displacement", "Photochemical"),
                    correctIndex = 1,
                    explanation = "Quicklime combines with water to form slaked lime, releasing immense heat."
                ),
                QuizQuestion(
                    id = "qz_ch10_2",
                    question = "Rusting of iron is an example of:",
                    options = listOf("Corrosion and Redox", "Decomposition", "Only reduction", "Neutralisation"),
                    correctIndex = 0,
                    explanation = "Iron reacts with oxygen and moisture to undergo oxidative corrosion."
                ),
                QuizQuestion(
                    id = "qz_ch10_3",
                    question = "A substance that loses electrons or gains oxygen during a reaction is said to be:",
                    options = listOf("Reduced", "Oxidised", "Neutralised", "Precipitated"),
                    correctIndex = 1,
                    explanation = "Oxidation is defined as the addition of oxygen or loss of electrons."
                )
            ),
            youtubeVideoTitle = "Chemical Reactions and Equations Class 10 Full Chapter",
            youtubeSearchQuery = "Chemical Reactions and Equations Class 10 One Shot"
        ),

        // ==================== CLASS 10 BIOLOGY ====================
        TopicModule(
            id = "m10_bio_life",
            classLevel = 10,
            subject = "Biology",
            board = "CBSE",
            topicTitle = "Life Processes: Nutrition & Respiration",
            chapterDescription = "Autotrophic and heterotrophic nutrition, human digestive system, aerobic vs anaerobic respiration, and ATP production.",
            formulas = listOf(
                "Photosynthesis: 6CO₂ + 12H₂O (Sunlight & Chlorophyll) -> C₆H₁₂O₆ + 6O₂ + 6H₂O",
                "Aerobic Respiration: Glucose -> Pyruvate -> CO₂ + H₂O + 38 ATP (Mitochondria)",
                "Anaerobic Respiration (Yeast): Glucose -> Ethanol + CO₂ + 2 ATP",
                "Anaerobic Respiration (Muscle): Glucose -> Lactic Acid + 2 ATP"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_bio10_1",
                    title = "Difference in Respiration Pathways",
                    problemStatement = "Explain what happens to pyruvate in our muscle cells during strenuous exercise and why cramps occur.",
                    difficulty = "Medium",
                    formulaUsed = "Breakdown of Pyruvate in lack of Oxygen",
                    stepByStepSolution = listOf(
                        "Step 1: During sudden vigorous physical exercise, our muscles demand more oxygen than blood can supply.",
                        "Step 2: Pyruvate is converted into a 3-carbon molecule called Lactic Acid.",
                        "Step 3: Accumulation of lactic acid in muscle cells causes fatigue and painful muscle cramps.",
                        "Step 4: Relief is obtained by hot water bath or massage which improves blood circulation and oxygen supply."
                    ),
                    finalAnswer = "Accumulation of Lactic Acid due to anaerobic breakdown of pyruvate causes cramps.",
                    hint = "Remember: Lack of oxygen in muscles produces lactic acid, while absence of oxygen in yeast produces ethanol.",
                    youtubeVideoQuery = "Life Processes Respiration Breakdown of Glucose Pathways Class 10"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_bio10_1",
                    question = "Where does the conversion of glucose into pyruvate occur in a cell?",
                    options = listOf("Mitochondria", "Cytoplasm", "Nucleus", "Ribosomes"),
                    correctIndex = 1,
                    explanation = "Glycolysis (glucose to pyruvate) takes place in the cytoplasm."
                ),
                QuizQuestion(
                    id = "qz_bio10_2",
                    question = "The enzyme present in saliva that breaks down starch into simple sugars is:",
                    options = listOf("Pepsin", "Salivary Amylase", "Trypsin", "Lipase"),
                    correctIndex = 1,
                    explanation = "Salivary amylase breaks starch into maltose in the mouth."
                ),
                QuizQuestion(
                    id = "qz_bio10_3",
                    question = "Which organ produces bile juice for the emulsification of fats?",
                    options = listOf("Pancreas", "Stomach", "Liver", "Gall bladder"),
                    correctIndex = 2,
                    explanation = "Liver produces bile juice, which is stored temporarily in the gallbladder."
                )
            ),
            youtubeVideoTitle = "Life Processes Class 10 Biology Full Chapter One Shot",
            youtubeSearchQuery = "Life Processes Class 10 Biology Full Chapter"
        ),

        // ==================== CLASS 9 MATHEMATICS ====================
        TopicModule(
            id = "m9_math_poly",
            classLevel = 9,
            subject = "Mathematics",
            board = "CBSE",
            topicTitle = "Polynomials & Factor Theorem",
            chapterDescription = "Zeros of polynomials, remainder theorem, factor theorem, and algebraic identities.",
            formulas = listOf(
                "(a + b)² = a² + 2ab + b²",
                "(a - b)² = a² - 2ab + b²",
                "a² - b² = (a - b)(a + b)",
                "a³ + b³ + c³ - 3abc = (a+b+c)(a²+b²+c² - ab - bc - ca)"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_m9_1",
                    title = "Evaluate using Identity",
                    problemStatement = "Evaluate 105 × 106 without direct multiplication.",
                    difficulty = "Easy",
                    formulaUsed = "(x + a)(x + b) = x² + (a + b)x + ab",
                    stepByStepSolution = listOf(
                        "Step 1: Write as (100 + 5)(100 + 6).",
                        "Step 2: Here x = 100, a = 5, b = 6.",
                        "Step 3: (100)² + (5 + 6)(100) + (5 × 6) = 10000 + 1100 + 30.",
                        "Step 4: Sum = 11130."
                    ),
                    finalAnswer = "11130",
                    hint = "Base number 100 makes the calculation fast and exact.",
                    youtubeVideoQuery = "Class 9 Maths Polynomials Algebraic Identities NCERT"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_m9_1",
                    question = "The degree of a non-zero constant polynomial is:",
                    options = listOf("1", "0", "Not defined", "2"),
                    correctIndex = 1,
                    explanation = "A constant like 5 can be written as 5x⁰, so degree is 0."
                ),
                QuizQuestion(
                    id = "qz_m9_2",
                    question = "If a + b + c = 0, then a³ + b³ + c³ equals:",
                    options = listOf("0", "3abc", "abc", "-3abc"),
                    correctIndex = 1,
                    explanation = "When a+b+c = 0, the identity simplifies directly to a³+b³+c³ = 3abc."
                )
            ),
            youtubeVideoTitle = "Class 9 Maths Polynomials One Shot",
            youtubeSearchQuery = "Class 9 Maths Polynomials Full Chapter One Shot"
        ),

        // ==================== CLASS 8 MATHEMATICS ====================
        TopicModule(
            id = "m8_math_linear",
            classLevel = 8,
            subject = "Mathematics",
            board = "CBSE",
            topicTitle = "Linear Equations in One Variable",
            chapterDescription = "Solving linear equations, transposing terms, cross-multiplication, and word problems.",
            formulas = listOf(
                "Linear Equation form: ax + b = c (a ≠ 0)",
                "Transposition: Add/Subtract same quantity on both sides"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_m8_1",
                    title = "Solve Linear Equation",
                    problemStatement = "Solve: 5x + 9 = 5 + 3x.",
                    difficulty = "Easy",
                    formulaUsed = "Group variable terms on LHS and constants on RHS",
                    stepByStepSolution = listOf(
                        "Step 1: Subtract 3x from both sides: 5x - 3x + 9 = 5.",
                        "Step 2: 2x + 9 = 5.",
                        "Step 3: Subtract 9 from both sides: 2x = 5 - 9 = -4.",
                        "Step 4: Divide by 2: x = -2."
                    ),
                    finalAnswer = "x = -2",
                    hint = "Move all x terms to the left and numbers to the right.",
                    youtubeVideoQuery = "Class 8 Maths Linear Equations NCERT Chapter 2"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_m8_1",
                    question = "The solution of 2x - 3 = 7 is:",
                    options = listOf("2", "5", "10", "4"),
                    correctIndex = 1,
                    explanation = "2x = 7 + 3 = 10 => x = 5."
                )
            ),
            youtubeVideoTitle = "Class 8 Maths Linear Equations in One Variable",
            youtubeSearchQuery = "Class 8 Maths Linear Equations in One Variable"
        ),

        // ==================== CLASS 6 MATHEMATICS ====================
        TopicModule(
            id = "m6_math_integers",
            classLevel = 6,
            subject = "Mathematics",
            board = "CBSE",
            topicTitle = "Integers & Number Line",
            chapterDescription = "Positive and negative numbers, representing integers on a number line, rules of integer arithmetic.",
            formulas = listOf(
                "Positive + Positive = Positive",
                "Negative + Negative = Negative (Add values, keep negative sign)",
                "Negative × Negative = Positive"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_m6_1",
                    title = "Integer Addition",
                    problemStatement = "Find the value of (-10) + (+15) + (-8).",
                    difficulty = "Easy",
                    formulaUsed = "Group negative terms first, then subtract from positive",
                    stepByStepSolution = listOf(
                        "Step 1: Combine negative integers: (-10) + (-8) = -18.",
                        "Step 2: Add (+15): -18 + 15 = -3."
                    ),
                    finalAnswer = "-3",
                    hint = "Debits of ₹10 and ₹8 make -₹18. Credit of ₹15 leaves -₹3.",
                    youtubeVideoQuery = "Class 6 Maths Integers Chapter 6 NCERT"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_m6_1",
                    question = "What is the predecessor of -9?",
                    options = listOf("-8", "-10", "8", "10"),
                    correctIndex = 1,
                    explanation = "Predecessor is obtained by subtracting 1: -9 - 1 = -10."
                )
            ),
            youtubeVideoTitle = "Class 6 Maths Integers NCERT",
            youtubeSearchQuery = "Class 6 Maths Integers NCERT"
        ),

        // ==================== CLASS 1 to 5 FOUNDATION ====================
        TopicModule(
            id = "m3_math_add_mult",
            classLevel = 3,
            subject = "Mathematics",
            board = "CBSE",
            topicTitle = "Multiplication Tables & Word Problems",
            chapterDescription = "Multiplication as repeated addition, word problems on equal groups, sharing.",
            formulas = listOf(
                "Multiplication = Repeated Addition (e.g. 4 × 3 = 3 + 3 + 3 + 3 = 12)",
                "Total Items = Number of Groups × Items per Group"
            ),
            practiceProblems = listOf(
                PracticeProblem(
                    id = "p_m3_1",
                    title = "Equal Sharing Word Problem",
                    problemStatement = "A teacher has 24 candies and wants to distribute them equally among 4 students. How many candies does each student get?",
                    difficulty = "Easy",
                    formulaUsed = "Total candies ÷ Number of students",
                    stepByStepSolution = listOf(
                        "Step 1: Total candies = 24.",
                        "Step 2: Divide by 4 students: 24 ÷ 4 = 6.",
                        "Step 3: Each student receives 6 candies."
                    ),
                    finalAnswer = "6 candies each",
                    hint = "Count by 4s: 4, 8, 12, 16, 20, 24 (6 times).",
                    youtubeVideoQuery = "Class 3 Maths Division Word Problems"
                )
            ),
            quiz = listOf(
                QuizQuestion(
                    id = "qz_m3_1",
                    question = "What is 7 × 8?",
                    options = listOf("54", "56", "48", "64"),
                    correctIndex = 1,
                    explanation = "7 multiplied by 8 equals 56."
                )
            ),
            youtubeVideoTitle = "Class 3 Maths Multiplication Fun Lesson",
            youtubeSearchQuery = "Class 3 Maths Multiplication Fun Lesson"
        )
    )

    fun getModules(classLevel: Int, subject: String): List<TopicModule> {
        val filtered = allModules.filter {
            it.classLevel == classLevel && (it.subject.equals(subject, ignoreCase = true) || subject == "All")
        }
        return if (filtered.isNotEmpty()) {
            filtered
        } else {
            // Return modules for that class or fallback modules for that subject
            allModules.filter { it.subject.equals(subject, ignoreCase = true) }
        }
    }

    val availableSubjects: List<String> = listOf(
        "Mathematics",
        "Physics",
        "Chemistry",
        "Biology"
    )
}
