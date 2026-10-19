package com.example.data.local

data class KitabEntry(
    val id: String,
    val title: String,
    val bookSource: String,
    val category: String, // "Hikmah & Suryani", "Fiqih & Usul", "Tasawwuf", "Nahwu/Shorof"
    val originalArabic: String,
    val harokatArabic: String,
    val transliteration: String,
    val translationPesantren: String,
    val scriptType: String, // "SURYANI_BIRHATIYAH" or "ARAB_GUNDUL"
    val description: String,
    val letterBreakdown: String = ""
)

object KitabLibraryData {
    val entries: List<KitabEntry> = listOf(
        KitabEntry(
            id = "birhatiyah_01",
            title = "Asma Birhatiyah 1-4 (Syamsul Ma'arif)",
            bookSource = "Syamsul Ma'arif al-Kubra - Syaikh Ahmad al-Buni",
            category = "Hikmah & Suryani",
            originalArabic = "برهتية كرير تتلية طوران",
            harokatArabic = "بَرْهَتِيَةٍ كَرِيْرٍ تَتْلِيَةٍ طَوْرَانٍ",
            transliteration = "Birhatiyyah, Karīr, Tatliyyah, Thawrān",
            translationPesantren = "(1) Birhatiyah: Quddusun (Yang Maha Suci). (2) Karir: Ya Allahu bi kulli syay-in (Wahai Dzat Yang Maha Mengetahui segala sesuatu). (3) Tatliyah: Al-Quddusu as-Subbūh (Maha Suci lagi Maha Bersih dari segala cela). (4) Thawran: Ya Hayyu Ya Qayyum (Wahai Dzat Yang Maha Hidup lagi Berdiri Sendiri). Disusun dengan gaya ilmu hikmah klasik untuk pelestarian khazanah manuskrip Islam.",
            scriptType = "SURYANI_BIRHATIYAH",
            description = "Empat asma pertama dari Qosam Birhatiyah kuno yang tertulis dalam dialek Suryani klasik, dipelihara dalam manuskrip Syamsul Ma'arif.",
            letterBreakdown = "• B-R-H-T-Y-H (ب-ر-ه-ت-ي-هـ) = 'Al-Quddus' (Kudus/Maha Suci)\n• K-R-Y-R (ك-ر-ي-ر) = 'Al-Ilah bi kulli syay-in' (Tuhan semesta ilmu)\n• T-T-L-Y-H (ت-ت-ل-ي-هـ) = 'As-Subbuh' (Maha Bertasbih seluruh alam)\n• Th-W-R-A-N (ط-و-ر-ا-ن) = 'Al-Hayyu Al-Qayyum' (Maha Kekal Berdiri Sendiri)"
        ),
        KitabEntry(
            id = "birhatiyah_02",
            title = "Asma Birhatiyah 5-8 (Syamsul Ma'arif)",
            bookSource = "Syamsul Ma'arif al-Kubra - Bab Qosam al-A'zhom",
            category = "Hikmah & Suryani",
            originalArabic = "مزجل بزجل ترقب برهش",
            harokatArabic = "مَزْجَلٍ بَزْجَلٍ تَرْقَبٍ بَرْهَشٍ",
            transliteration = "Mazjalin, Bazjalin, Tarqabin, Barhasyin",
            translationPesantren = "(5) Mazjal: Ya Qayyum (Wahai Dzat Pemelihara Alam). (6) Bazjal: Ya Wadud (Wahai Dzat Yang Maha Pengasih). (7) Tarqab: Ya Salam (Wahai Dzat Pemberi Keselamatan). (8) Barhasy: Ya Muqtadir (Wahai Dzat Yang Maha Kuasa dan Menundukkan). Khazanah bahasa Suryani klasik dalam tradisi naskah pesantren.",
            scriptType = "SURYANI_BIRHATIYAH",
            description = "Asma kelima hingga kedelapan dari perjanjian Birhatiyah kubra dengan tafsir nama ketuhanan menurut Syaikh Al-Buni.",
            letterBreakdown = "• M-Z-J-L (م-ز-ج-ل) = 'Ya Qayyum'\n• B-Z-J-L (ب-ز-ج-ل) = 'Ya Wadud'\n• T-R-Q-B (ت-ر-ق-ب) = 'Ya Salam'\n• B-R-H-Sh (ب-ر-ه-ش) = 'Ya Muqtadir'"
        ),
        KitabEntry(
            id = "safinah_01",
            title = "Fasal Arkanul Islam (Kitab Safinah)",
            bookSource = "Safinatun Najah - Syaikh Salim bin Sumair al-Hadhrami",
            category = "Fiqih & Usul",
            originalArabic = "فصل اركان الاسلام خمسة شهادة ان لا اله الا الله وان محمدا رسول الله واقام الصلاة وايتاء الزكاة وصوم رمضان وحج البيت من استطاع اليه سبيلا",
            harokatArabic = "فَصْلٌ أَرْكَانُ الْإِسْلَامِ خَمْسَةٌ: شَهَادَةُ أَنْ لَا إِلَهَ إِلَّا اللهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللهِ، وَإِقَامُ الصَّلَاةِ، وَإِيتَاءُ الزَّكَاةِ، وَصَوْمُ رَمَضَانَ، وَحَجُّ الْبَيْتِ مَنِ اسْتَطَاعَ إِلَيْهِ سَبِيلًا.",
            transliteration = "Faslun: Arkānul Islāmi khamsatun: syahādatu an lā ilāha illallāh wa anna Muhammadar Rasūlullāh, wa iqāmus-salāh, wa ītā'uz-zakāh, wa shawmu Ramadhān, wa hajjul bayti manis-tathā'a ilayhi sabīlā.",
            translationPesantren = "Utawi iki iku fasal suwiji. Utawi rukun Islam iku ana lima perkoro: (1) Nyekseni satuhune ora ana Pengeran kang haq disembah kelawan bener anging Gusti Allah, lan satuhune Kanjeng Nabi Muhammad iku utusane Allah, (2) Lan ngedekake sholat lima wektu, (3) Lan aweh zakat, (4) Lan pasa ing wulan Ramadhan, (5) Lan munggah kaji marang Baitullah kanggone wong kang kuwasa dalane.",
            scriptType = "ARAB_GUNDUL",
            description = "Matan awal kitab fiqih dasar Safinatun Najah tentang lima pondasi rukun Islam dalam dialek Arab Gundul.",
            letterBreakdown = "Teks fiqih klasik bermakna gandul pesantren."
        ),
        KitabEntry(
            id = "fathul_qorib_01",
            title = "Mukaddimah Kitab Thaharah",
            bookSource = "Fathul Qorib al-Mujib - Ibnu Qasim al-Ghazi",
            category = "Fiqih & Usul",
            originalArabic = "كتاب احكام الطهارة المياه التي يجوز بها التطهير سبع مياه ماء السماء وماء البحر وماء النهر وماء البئر وماء العين وماء الثلج وماء البرد",
            harokatArabic = "كِتَابُ أَحْكَامِ الطَّهَارَةِ. اَلْمِيَاهُ الَّتِي يَجُوزُ بِهَا التَّطْهِيرُ سَبْعُ مِيَاهٍ: مَاءُ السَّمَاءِ، وَمَاءُ الْبَحْرِ، وَمَاءُ النَّهْرِ، وَمَاءُ الْبِئْرِ، وَمَاءُ الْعَيْنِ، وَمَاءُ الثَّلْجِ، وَمَاءُ الْبَرَدِ.",
            transliteration = "Kitābu ahkāmit-thahārah. Al-miyāhul-latī yajūzu bihat-tath-hīru sab'u miyāhin: mā'us-samā'i, wa mā'ul-bahri, wa mā'un-nahri, wa mā'ul-bi'ri, wa mā'ul-'ayni, wa mā'uts-tsalji, wa mā'ul-barad.",
            translationPesantren = "Kitab nerangake piro-piro hukume sesuci. Utawi piro-piro banyu kang wenang kanggone sesuci iku ana pitung wernaning banyu: (1) banyu langit (udan), (2) banyu segara, (3) banyu kali, (4) banyu sumur, (5) banyu sumber mata air, (6) banyu salju/es, (7) banyu embun/hujan es.",
            scriptType = "ARAB_GUNDUL",
            description = "Pembahasan 7 jenis air suci mensucikan dalam Kitab Kuning Fathul Qorib.",
            letterBreakdown = "Kosakata fiqih madzhab Syafi'i."
        ),
        KitabEntry(
            id = "hikam_01",
            title = "Hikmah Pertama: Bersandar pada Amal",
            bookSource = "Al-Hikam - Syaikh Ibnu Atha'illah as-Sakandari",
            category = "Tasawwuf",
            originalArabic = "من علامات الاعتماد على العمل نقصان الرجاء عند وجود الزلل",
            harokatArabic = "مِنْ عَلَامَاتِ الِاعْتِمَادِ عَلَى الْعَمَلِ، نُقْصَانُ الرَّجَاءِ عِنْدَ وُجُودِ الزَّلَلِ.",
            transliteration = "Min 'alāmātil-i'timādi 'alal-'amali, nuqshānur-rajā'i 'inda wujūdiz-zalali.",
            translationPesantren = "Sebagian saking piro-piro tandane wong kang kumendel utawa gondhelan marang amale dewe (ora marang fadhol kanugrahane Gusti Allah), yaiku kurange pengarep-arep (rahmatipun Allah) nalika ana kaluputan utawa tumiba ing dosa.",
            scriptType = "ARAB_GUNDUL",
            description = "Aforisme pembuka tasawwuf tingkat tinggi tentang tawakkal murni dan bahaya ujub.",
            letterBreakdown = "Kaidah Ma'rifatullah dalam tradisi pesantren Nusantara."
        ),
        KitabEntry(
            id = "syamsul_maarif_02",
            title = "Rajah & Wafaq Sirrul Huruf",
            bookSource = "Syamsul Ma'arif al-Kubra - Bab Asmaul Husna",
            category = "Hikmah & Suryani",
            originalArabic = "شمخاهر شمخاهير شمخاهير بكهطهونية بشكليخ",
            harokatArabic = "شَمْخَاهِرٍ شَمْخَاهِيْرٍ بَكَهْطَهُوْنِيَةٍ بِشَكْلِيْخٍ",
            transliteration = "Syamkhāhirin, Syamkhāhīrin, Bakahthahūniyah, Bisyaklīkh",
            translationPesantren = "Teks naskah Garshuni-Hikmah kuno: (1) Syamkhahir: Ta'alayta ya 'Aliyyu ya 'Azhim (Maha Luhur Engkau wahai Dzat Yang Maha Tinggi lagi Maha Agung). (2) Bakahthahuniyah: Ya Daimu (Wahai Dzat Yang Maha Kekal Abadi). (3) Bisyaklikh: Ya Qadiru Ya Kafi (Wahai Dzat Yang Maha Menentukan lagi Mencukupi). Diterjemahkan untuk preservasi kajian filologi manuskrip Islam.",
            scriptType = "SURYANI_BIRHATIYAH",
            description = "Asma Suryani kuno penutup bait Birhatiyah yang sering dijumpai pada manuskrip wafaq dan rajah kuno.",
            letterBreakdown = "• Sy-M-Kh-A-H-R = 'Ya 'Aliyyu Ya 'Azhim'\n• B-K-H-Th-H-W-N-Y-H = 'Ya Daim'\n• B-Sh-K-L-Y-Kh = 'Ya Qadir Ya Kafi'"
        )
    )
}
