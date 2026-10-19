package com.example.data.ai

object ScriptDetector {
    const val TYPE_ARAB_GUNDUL = "ARAB_GUNDUL"
    const val TYPE_SURYANI_BIRHATIYAH = "SURYANI_BIRHATIYAH"

    private val BIRHATIYAH_KEYWORDS = listOf(
        "برهتية", "كرير", "تتلية", "طوران", "مزجل", "بزجل", "ترقب", "برهش",
        "غلمش", "خوطير", "قلنهود", "برشان", "كظهير", "نموشلخ", "برهيولا",
        "بشكيلخ", "قزمز", "انغلليط", "قبرات", "غياها", "كيدهولا", "شمخاهر",
        "شمخاهير", "بكهطهونية", "بشكليخ", "طونش", "سريانية", "بيرهتية",
        "birhatiyah", "karir", "tatliyah", "thawran", "mazjal", "bazjal", "barhasy",
        "suryani", "syamsul maarif", "hikmah"
    )

    fun detectScriptType(text: String): String {
        val normalized = text.lowercase().trim()

        // 1. Check for Syriac Unicode block (U+0700 to U+074F)
        val hasSyriacUnicode = text.any { it in '\u0700'..'\u074F' }
        if (hasSyriacUnicode) {
            return TYPE_SURYANI_BIRHATIYAH
        }

        // 2. Check for classical Birhatiyah names & roots
        for (keyword in BIRHATIYAH_KEYWORDS) {
            if (normalized.contains(keyword.lowercase())) {
                return TYPE_SURYANI_BIRHATIYAH
            }
        }

        // 3. Check for standard Arabic characters (U+0600 to U+06FF)
        val arabicCharCount = text.count { it in '\u0600'..'\u06FF' }
        if (arabicCharCount > 0) {
            // Count harakat (tashkeel): 064B - 0652
            val harakatCount = text.count { it in '\u064B'..'\u0652' }
            // If less than 15% vowels relative to Arabic characters, it is classical Arab Gundul
            return TYPE_ARAB_GUNDUL
        }

        return TYPE_ARAB_GUNDUL
    }
}
