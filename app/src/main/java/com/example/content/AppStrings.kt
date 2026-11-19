package com.example.content

import com.example.prefs.AppLanguage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AyatWithTarjama(
    val id: Int,
    val arabic: String,
    val englishTarjama: String,
    val romanUrduTarjama: String,
    val urduTarjama: String,
    val reference: String,
    val helpContextEn: String,
    val helpContextRomanUr: String,
    val helpContextUr: String
)

data class DailyZikrItem(
    val id: Int,
    val arabic: String,
    val transliteration: String,
    val meaningEn: String,
    val meaningRomanUr: String,
    val meaningUr: String,
    val targetCount: Int = 33
)

data class LocalizedUiCopy(
    val appTitle: String,
    val greetingPrefix: String,
    val allahWithYouBadge: String,
    val creatorCredit: String,
    val creatorSubtitle: String,
    val strikeFreeTimerTitle: String,
    val setStartDateTime: String,
    val cleanDaySingular: String,
    val cleanDaysPlural: String,
    val challengeDayLabel: String,
    val todayLabel: String,
    val timerMotivations: List<String>,
    val urgeTimerMotivation: String,
    val hoursLabel: String,
    val minutesLabel: String,
    val secondsLabel: String,
    val emergencyBadge: String,
    val urgeCardTitle: String,
    val urgeCardSubtitle: String,
    val urgeHelpButton: String,
    val fabUrgeHelp: String,
    val dailyZikrTitle: String,
    val dailyZikrTapHint: String,
    val dailyZikrNextButton: String,
    val dailyZikrResetButton: String,
    val dailyZikrCompletedBadge: String,
    val ayatSectionTitle: String,
    val nextAyah: String,
    val tarjamaLabel: String,
    val motivationSectionTitle: String,
    val nextMotivation: String,
    val guideShortcutButton: String,
    val slipResetButton: String,
    val tabHome: String,
    val tabGuide: String,
    val tabMilestones: String,
    val guideHeaderTag: String,
    val guideHeaderTitle: String,
    val guideHeaderSubtitle: String,
    val urgeRescueTitle: String,
    val urgeRescueHeroTitle: String,
    val urgeRescueHeroSub: String,
    val urgeBreathingHeader: String,
    val urgeTasbihHeader: String,
    val urgeStepsHeader: String,
    val urgeVictoryButton: String,
    val appearanceCardTitle: String,
    val languageLabel: String,
    val customColorLabel: String,
    val themeModeLabel: String,
    val aboutCreatorTitle: String,
    val aboutCreatorBody: String,
    val apkInstallTitle: String,
    val apkInstallBody: String
)

object AppLocalization {

    val dailyZikrList: List<DailyZikrItem> = listOf(
        DailyZikrItem(
            id = 1,
            arabic = "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
            transliteration = "Astaghfirullah al-Azim wa atubu ilayh",
            meaningEn = "I seek forgiveness from Allah the Almighty and turn to Him in repentance.",
            meaningRomanUr = "Main Azmat wale Allah se maafi maangta hoon aur Us ki taraf tauba karta hoon.",
            meaningUr = "میں عظمت والے اللہ سے بخشش مانگتا ہوں اور اسی کی طرف توبہ کرتا ہوں۔",
            targetCount = 33
        ),
        DailyZikrItem(
            id = 2,
            arabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ، سُبْحَانَ اللَّهِ الْعَظِيمِ",
            transliteration = "SubhanAllahi wa bihamdihi, SubhanAllahil-Azim",
            meaningEn = "Glory and praise be to Allah; Glory be to Allah the Supreme.",
            meaningRomanUr = "Allah paak hai apni tareef ke saath, Allah bohot azmat wala paak hai.",
            meaningUr = "اللہ اپنی تعریف سمیت پاک ہے، عظمت والا اللہ ہر عیب سے پاک ہے۔",
            targetCount = 33
        ),
        DailyZikrItem(
            id = 3,
            arabic = "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ",
            transliteration = "La hawla wa la quwwata illa billah",
            meaningEn = "There is no power and no strength except through Allah.",
            meaningRomanUr = "Gunah se bachne aur neki karne ki koi taqat nahi magar Allah ki madad se.",
            meaningUr = "گناہ سے بچنے اور نیکی کرنے کی کوئی طاقت نہیں مگر اللہ کی مدد سے۔",
            targetCount = 33
        ),
        DailyZikrItem(
            id = 4,
            arabic = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            transliteration = "Hasbunallahu wa ni'mal wakeel",
            meaningEn = "Allah is sufficient for us, and He is the best Disposer of affairs.",
            meaningRomanUr = "Hamare liye Allah kaafi hai aur Woh behtareen kaarsaaz hai.",
            meaningUr = "ہمارے لیے اللہ کافی ہے اور وہ بہترین کارساز ہے۔",
            targetCount = 33
        ),
        DailyZikrItem(
            id = 5,
            arabic = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ",
            transliteration = "Ya Hayyu Ya Qayyum bi-rahmatika astagheeth",
            meaningEn = "O Ever-Living, O Sustainer, in Your mercy I seek relief and help.",
            meaningRomanUr = "Ae Hamesha Zinda aur Qaim rehne wale, Teri rehmat se madad maangta hoon.",
            meaningUr = "اے ہمیشہ زندہ اور قائم رہنے والے، تیری رحمت سے مدد طلب کرتا ہوں۔",
            targetCount = 33
        ),
        DailyZikrItem(
            id = 6,
            arabic = "لَا إِلَٰهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
            transliteration = "La ilaha illAllahu wahdahu la sharika lah",
            meaningEn = "There is no deity worthy of worship except Allah alone, without partner.",
            meaningRomanUr = "Allah ke siwa koi ma'bood nahi, Woh akela hai aur Us ka koi shareek nahi.",
            meaningUr = "اللہ کے سوا کوئی معبود نہیں، وہ اکیلا ہے اس کا کوئی شریک نہیں۔",
            targetCount = 100
        )
    )

    fun dateKeyFor(millis: Long): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date(millis))

    fun formatDayAndDate(millis: Long, language: AppLanguage): String {
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        val dayName = when (cal.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> when (language) {
                AppLanguage.ENGLISH -> "Monday"
                AppLanguage.ROMAN_URDU -> "Peer (Monday)"
                AppLanguage.URDU -> "پیر"
            }
            Calendar.TUESDAY -> when (language) {
                AppLanguage.ENGLISH -> "Tuesday"
                AppLanguage.ROMAN_URDU -> "Mangal (Tuesday)"
                AppLanguage.URDU -> "منگل"
            }
            Calendar.WEDNESDAY -> when (language) {
                AppLanguage.ENGLISH -> "Wednesday"
                AppLanguage.ROMAN_URDU -> "Budh (Wednesday)"
                AppLanguage.URDU -> "بدھ"
            }
            Calendar.THURSDAY -> when (language) {
                AppLanguage.ENGLISH -> "Thursday"
                AppLanguage.ROMAN_URDU -> "Jumerat (Thursday)"
                AppLanguage.URDU -> "جمعرات"
            }
            Calendar.FRIDAY -> when (language) {
                AppLanguage.ENGLISH -> "Jumu'ah (Friday)"
                AppLanguage.ROMAN_URDU -> "Juma (Friday)"
                AppLanguage.URDU -> "جمعہ"
            }
            Calendar.SATURDAY -> when (language) {
                AppLanguage.ENGLISH -> "Saturday"
                AppLanguage.ROMAN_URDU -> "Hafta (Saturday)"
                AppLanguage.URDU -> "ہفتہ"
            }
            else -> when (language) {
                AppLanguage.ENGLISH -> "Sunday"
                AppLanguage.ROMAN_URDU -> "Itwar (Sunday)"
                AppLanguage.URDU -> "اتوار"
            }
        }
        val datePart = SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date(millis))
        return "$dayName • $datePart"
    }

    val helpAyatList: List<AyatWithTarjama> = listOf(
        AyatWithTarjama(
            id = 1,
            arabic = "يَا أَيُّهَا الَّذِينَ آمَنُوا اسْتَعِينُوا بِالصَّبْرِ وَالصَّلَاةِ ۚ إِنَّ اللَّهَ مَعَ الصَّابِرِينَ",
            englishTarjama = "O you who have believed, seek help through patience and prayer. Indeed, Allah is with the patient.",
            romanUrduTarjama = "Ae imaan walon! Sabr aur namaz ke zariye madad hasil karo. Beshak Allah sabr karne walon ke saath hai.",
            urduTarjama = "اے ایمان والو! صبر اور نماز سے مدد حاصل کرو، بے شک اللہ صبر کرنے والوں کے ساتھ ہے۔",
            reference = "Surah Al-Baqarah 2:153",
            helpContextEn = "When temptation feels intense, pause for Sabr and stand for Salah—Allah's help arrives for the patient.",
            helpContextRomanUr = "Jab urge tez ho, thori der sabr aur namaz ka sahara lo—Allah ki madad zaroor aati hai.",
            helpContextUr = "جب خواہش کا دباؤ بڑھے تو صبر اور نماز کا سہارا لیں، اللہ کی مدد ضرور پہنچتی ہے۔"
        ),
        AyatWithTarjama(
            id = 2,
            arabic = "وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
            englishTarjama = "And whoever relies upon Allah – then He is sufficient for him.",
            romanUrduTarjama = "Aur jo shakhs Allah par bharosa kare to Allah us ke liye kaafi hai.",
            urduTarjama = "اور جو اللہ پر بھروسہ کرے تو وہ اس کے لیے کافی ہے۔",
            reference = "Surah At-Talaq 65:3",
            helpContextEn = "You do not have to defeat this habit by your willpower alone—put your trust in Allah.",
            helpContextRomanUr = "Sirf apni taqat se nahi, Allah par tawakkal karke laro—Wohi kaafi hai.",
            helpContextUr = "صرف اپنی طاقت پر نہیں بلکہ اللہ پر بھروسہ کر کے مقابلہ کریں، وہی کافی ہے۔"
        ),
        AyatWithTarjama(
            id = 3,
            arabic = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا",
            englishTarjama = "Say, 'O My servants who have transgressed against themselves, do not despair of the mercy of Allah. Indeed, Allah forgives all sins.'",
            romanUrduTarjama = "Keh do: Ae mere bandon jinhon ne apni jaanon par zyadti ki, Allah ki rehmat se mayoos na ho. Beshak Allah saare gunah maaf farma deta hai.",
            urduTarjama = "فرما دیجیے: اے میرے بندو جنہوں نے اپنی جانوں پر زیادتی کی، اللہ کی رحمت سے مایوس نہ ہو۔ بے شک اللہ تمام گناہ بخش دیتا ہے۔",
            reference = "Surah Az-Zumar 39:53",
            helpContextEn = "Never let guilt push you away from Allah. His mercy is always bigger than your past.",
            helpContextRomanUr = "Pichli ghaltiyon ki wajah se kabhi mayoos na ho. Allah ki rehmat har gunah se bari hai.",
            helpContextUr = "پچھلی خطاؤں کی وجہ سے کبھی مایوس نہ ہوں۔ اللہ کی رحمت ہر گناہ سے بڑی ہے۔"
        ),
        AyatWithTarjama(
            id = 4,
            arabic = "وَهُوَ مَعَكُمْ أَيْنَ مَا كُنتُمْ ۚ وَاللَّهُ بِمَا تَعْمَلُونَ بَصِيرٌ",
            englishTarjama = "And He is with you wherever you are. And Allah, of what you do, is Seeing.",
            romanUrduTarjama = "Aur tum jahan kahin bhi ho, Woh tumhare saath hai. Aur jo kuch tum karte ho Allah use dekh raha hai.",
            urduTarjama = "اور تم جہاں کہیں بھی ہو، وہ تمہارے ساتھ ہے، اور جو کچھ تم کرتے ہو اللہ اسے دیکھ رہا ہے۔",
            reference = "Surah Al-Hadid 57:4",
            helpContextEn = "Even in a locked room at midnight, Allah is with you and sees your quiet sacrifice.",
            helpContextRomanUr = "Tanhai ke waqt bhi yaad rakho ke Allah tumhare saath hai aur tumhari koshish dekh raha hai.",
            helpContextUr = "تنہائی کے وقت بھی یاد رکھیں کہ اللہ آپ کے ساتھ ہے اور آپ کی کوشش دیکھ رہا ہے۔"
        ),
        AyatWithTarjama(
            id = 5,
            arabic = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            englishTarjama = "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.",
            romanUrduTarjama = "Pas beshak har mushkil ke saath aasani hai. Beshak har mushkil ke saath aasani hai.",
            urduTarjama = "پس بے شک مشکل کے ساتھ آسانی ہے، بے شک مشکل کے ساتھ آسانی ہے۔",
            reference = "Surah Ash-Sharh 94:5-6",
            helpContextEn = "The heaviness of an urge is temporary. Relief and peace are promised right alongside the struggle.",
            helpContextRomanUr = "Ye urge ki sakhti waqti hai. Allah ka waada hai ke mushkil ke saath aasani zaroor aati hai.",
            helpContextUr = "خواہش کی یہ سختی عارضی ہے۔ اللہ کا وعدہ ہے کہ مشکل کے ساتھ آسانی ضرور آتی ہے۔"
        ),
        AyatWithTarjama(
            id = 6,
            arabic = "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا",
            englishTarjama = "Allah does not charge a soul except [with that within] its capacity.",
            romanUrduTarjama = "Allah kisi jaan par us ki taaqat se zyada bojh nahi daalta.",
            urduTarjama = "اللہ کسی جان پر اس کی طاقت سے زیادہ بوجھ نہیں ڈالتا۔",
            reference = "Surah Al-Baqarah 2:286",
            helpContextEn = "You have the strength inside you—by Allah's help—to say no to this urge.",
            helpContextRomanUr = "Tumhare andar Allah ne itni himmat rakhi hai ke tum is urge ko hara sako.",
            helpContextUr = "اللہ نے آپ کے اندر اتنی ہمت رکھی ہے کہ آپ اس خواہش پر قابو پا سکیں۔"
        ),
        AyatWithTarjama(
            id = 7,
            arabic = "وَالَّذِينَ جَاهَدُوا فِينَا لَنَهْدِيَنَّهُمْ سُبُلَنَا",
            englishTarjama = "And those who strive for Us – We will surely guide them to Our ways.",
            romanUrduTarjama = "Aur jo log Hamari raah mein koshish karte hain, Hum zaroor unhen Apne raaste dikha dete hain.",
            urduTarjama = "اور جو لوگ ہماری راہ میں کوشش کرتے ہیں، ہم ضرور انہیں اپنے راستے دکھا دیتے ہیں۔",
            reference = "Surah Al-Ankabut 29:69",
            helpContextEn = "Every honest effort you make to stay clean opens new doors of guidance and peace.",
            helpContextRomanUr = "Paak rehne ke liye tumhari har koshish par Allah hidayat aur sukoon ke raaste khol deta hai.",
            helpContextUr = "پاک رہنے کے لیے آپ کی ہر کوشش پر اللہ ہدایت اور سکون کے راستے کھول دیتا ہے۔"
        )
    )

    fun getUiCopy(language: AppLanguage): LocalizedUiCopy = when (language) {
        AppLanguage.ENGLISH -> LocalizedUiCopy(
            appTitle = "Allah always with me",
            greetingPrefix = "Assalamu Alaikum,",
            allahWithYouBadge = "Allah is with you",
            creatorCredit = "Created by Muhammad Waqas",
            creatorSubtitle = "Built with sincerity to help believers overcome strikes & urges with Allah's help.",
            strikeFreeTimerTitle = "STRIKE-FREE TIMER",
            setStartDateTime = "Set Start Date & Time",
            cleanDaySingular = "CLEAN DAY",
            cleanDaysPlural = "CLEAN DAYS",
            challengeDayLabel = "Day",
            todayLabel = "Today",
            timerMotivations = listOf(
                "One clean minute at a time — Allah sees your quiet struggle.",
                "Stay steady right now; peace awaits on the other side of Sabr.",
                "You are stronger than this urge because Allah is with you.",
                "Guard this moment for Allah, and He will fill your heart with light."
            ),
            urgeTimerMotivation = "Just 60 seconds of Sabr — Allah is carrying you through this wave.",
            hoursLabel = "Hours",
            minutesLabel = "Minutes",
            secondsLabel = "Seconds",
            emergencyBadge = "EMERGENCY RESCUE",
            urgeCardTitle = "Feeling a Strong Urge Right Now?",
            urgeCardSubtitle = "Do not fight alone. Tap emergency rescue for breathing, Dhikr & immediate relief steps.",
            urgeHelpButton = "Emergency Urge Help — Tap Now",
            fabUrgeHelp = "Urge SOS",
            dailyZikrTitle = "DAILY ZIKR TASBIH",
            dailyZikrTapHint = "Tap with your heart to remember Allah",
            dailyZikrNextButton = "Change Zikr",
            dailyZikrResetButton = "Reset",
            dailyZikrCompletedBadge = "Daily Goal Complete — Alhamdulillah",
            ayatSectionTitle = "Ayat of Allah's Help & Tarjama",
            nextAyah = "Next Ayah",
            tarjamaLabel = "Tarjama:",
            motivationSectionTitle = "Allah's Help & Motivation",
            nextMotivation = "Next",
            guideShortcutButton = "11-Episode Guide",
            slipResetButton = "Had a Slip?",
            tabHome = "Sanctuary",
            tabGuide = "11 Episodes",
            tabMilestones = "Milestones",
            guideHeaderTag = "11-EPISODE RECOVERY GUIDE",
            guideHeaderTitle = "Healing Heart & Soul With Allah",
            guideHeaderSubtitle = "Tap any episode card below to expand its spiritual anchor, emotional motivation, and steps for Allah's help.",
            urgeRescueTitle = "Urge Rescue Sanctuary",
            urgeRescueHeroTitle = "Pause. Allah Sees Your Effort.",
            urgeRescueHeroSub = "This urge peaks in minutes. Follow these steps right now to break free.",
            urgeBreathingHeader = "STEP 1: 60-SECOND WAVE BREATHING",
            urgeTasbihHeader = "STEP 2: TAP TASBIH TO CALL ON ALLAH",
            urgeStepsHeader = "STEP 3: URGE RELIEF ACTIONS (CHUTKARA)",
            urgeVictoryButton = "Alhamdulillah, I Overcame This Urge",
            appearanceCardTitle = "Language & Custom Color",
            languageLabel = "App Language",
            customColorLabel = "Custom Sanctuary Color",
            themeModeLabel = "Brightness Mode",
            aboutCreatorTitle = "About & Developer Credit",
            aboutCreatorBody = "This application \"Allah always with me\" was created by Muhammad Waqas to support recovery from strikes and urges through patience, practical steps, and remembrance of Allah.",
            apkInstallTitle = "Install & Export APK",
            apkInstallBody = "To install on another phone or publish to Play Store, use the AI Studio Settings menu (Generate APK / AAB or Export Project) to download the compiled package."
        )

        AppLanguage.ROMAN_URDU -> LocalizedUiCopy(
            appTitle = "Allah always with me",
            greetingPrefix = "Assalamu Alaikum,",
            allahWithYouBadge = "Allah saath hai",
            creatorCredit = "Banai hai: Muhammad Waqas",
            creatorSubtitle = "Strike aur urge se bachne aur Allah ki madad yaad dilane ke liye Muhammad Waqas ne banai.",
            strikeFreeTimerTitle = "STRIKE-FREE CHALLENGE TIMER",
            setStartDateTime = "Start Date & Time Set Karein",
            cleanDaySingular = "PAAK DIN",
            cleanDaysPlural = "PAAK DIN",
            challengeDayLabel = "Din",
            todayLabel = "Aaj ka Din",
            timerMotivations = listOf(
                "Bas is ek lamhe par sabr kar lo — Allah tumhari har koshish dekh raha hai.",
                "Har guzarta second tumhari jeet aur dil ke sukoon ka saboot hai.",
                "Himmat mat haro, Allah ki madad har waqt tumhare bilkul qareeb hai.",
                "Allah ki khatir ye pal bacha lo, Woh tumhein behtareen sukoon dega."
            ),
            urgeTimerMotivation = "Sirf 60 second ka sabr — Allah is mushkil lamhe mein tumhare saath hai.",
            hoursLabel = "Ghante",
            minutesLabel = "Minute",
            secondsLabel = "Second",
            emergencyBadge = "EMERGENCY MADAD",
            urgeCardTitle = "Abhi Urge Mehsoos Ho Rahi Hai?",
            urgeCardSubtitle = "Akele mat laro. Emergency button dabayein aur urge se chutkara ka tareeqa dekhein.",
            urgeHelpButton = "Urge Emergency Button — Madad Lein",
            fabUrgeHelp = "Urge Madad",
            dailyZikrTitle = "DAILY ZIKR (ROZANA TASBIH)",
            dailyZikrTapHint = "Dil se tap karein aur Allah ko yaad karein",
            dailyZikrNextButton = "Zikr Badlein",
            dailyZikrResetButton = "Reset",
            dailyZikrCompletedBadge = "Aaj ka Hadaf Poora — Alhamdulillah",
            ayatSectionTitle = "Madad ke Liye Ayat aur Tarjama",
            nextAyah = "Agli Ayat",
            tarjamaLabel = "Tarjama:",
            motivationSectionTitle = "Allah ki Madad aur Motivation",
            nextMotivation = "Agla",
            guideShortcutButton = "11 Episode Guide",
            slipResetButton = "Slip Ho Gaya?",
            tabHome = "Home",
            tabGuide = "11 Episodes",
            tabMilestones = "Safar & Settings",
            guideHeaderTag = "11-EPISODE RECOVERY GUIDE",
            guideHeaderTitle = "Allah ki Madad se Dil aur Rooh ki Shifa",
            guideHeaderSubtitle = "Har episode card ko tap karke kholein—ismein emotional motivation, Allah ki madad aur amal ke steps hain.",
            urgeRescueTitle = "Urge se Chutkara",
            urgeRescueHeroTitle = "Ruk Jayein. Allah Aap ki Koshish Dekh Raha Hai.",
            urgeRescueHeroSub = "Urge sirf chand minute rehti hai. Neeche diye gaye steps par abhi amal karein.",
            urgeBreathingHeader = "STEP 1: 60-SECOND SAANS AUR SABR",
            urgeTasbihHeader = "STEP 2: ALLAH KO PUKAREIN (TASBIH)",
            urgeStepsHeader = "STEP 3: URGE SE CHUTKARA KA TAREEQA",
            urgeVictoryButton = "Alhamdulillah, Main Ne Urge Hara Di",
            appearanceCardTitle = "Zaban (Language) aur Custom Color",
            languageLabel = "Language Chunein",
            customColorLabel = "Apna Pasandeeda Color Chunein",
            themeModeLabel = "Light / Dark Mode",
            aboutCreatorTitle = "Creator ka Hawala",
            aboutCreatorBody = "Ye application \"Allah always with me\" Muhammad Waqas ne banai hai taake mushkil waqt aur urge ke dauran Allah ki madad aur sabr se kamyabi mile.",
            apkInstallTitle = "APK Install aur Download",
            apkInstallBody = "Phone par install karne ya publish karne ke liye AI Studio ke Settings menu se Generate APK / AAB ya Export Project istemal karein."
        )

        AppLanguage.URDU -> LocalizedUiCopy(
            appTitle = "Allah always with me",
            greetingPrefix = "السلام علیکم،",
            allahWithYouBadge = "اللہ ساتھ ہے",
            creatorCredit = "تیار کردہ: محمد وقاص",
            creatorSubtitle = "گناہوں اور بری عادتوں سے بچنے اور اللہ کی مدد یاد دلانے کے لیے محمد وقاص کی تیار کردہ ایپ۔",
            strikeFreeTimerTitle = "پاکیزہ سفر کا وقت",
            setStartDateTime = "تاریخ اور وقت مقرر کریں",
            cleanDaySingular = "پاک دن",
            cleanDaysPlural = "پاک دن",
            challengeDayLabel = "دن",
            todayLabel = "آج کا دن",
            timerMotivations = listOf(
                "بس اس ایک لمحے صبر کر لیں — اللہ آپ کی ہر کوشش دیکھ رہا ہے۔",
                "ہر گزرتا سیکنڈ آپ کی کامیابی اور دل کے سکون کی علامت ہے۔",
                "ہمت نہ ہاریں، اللہ کی مدد ہر لمحے آپ کے ساتھ ہے۔",
                "اللہ کی رضا کے لیے اس لمحے کو بچا لیں، وہ آپ کو بہترین سکون دے گا۔"
            ),
            urgeTimerMotivation = "صرف 60 سیکنڈ کا صبر — اللہ اس مشکل لمحے میں آپ کے ساتھ ہے۔",
            hoursLabel = "گھنٹے",
            minutesLabel = "منٹ",
            secondsLabel = "سیکنڈ",
            emergencyBadge = "ایمرجنسی مدد",
            urgeCardTitle = "کیا اس وقت شدید خواہش محسوس ہو رہی ہے؟",
            urgeCardSubtitle = "اکیلے نہ لڑیں۔ ایمرجنسی بٹن دبائیں اور فوراً بچاؤ کا طریقہ اختیار کریں۔",
            urgeHelpButton = "ایمرجنسی مدد — ابھی دبائیں",
            fabUrgeHelp = "فوری مدد",
            dailyZikrTitle = "روزانہ ذکر و تسبیح",
            dailyZikrTapHint = "دل سے اللہ کو یاد کرنے کے لیے یہاں ٹیپ کریں",
            dailyZikrNextButton = "ذکر بدلیں",
            dailyZikrResetButton = "ری سیٹ",
            dailyZikrCompletedBadge = "آج کا ہدف مکمل — الحمدللہ",
            ayatSectionTitle = "مدد کے لیے قرآنی آیات مع ترجمہ",
            nextAyah = "اگلی آیت",
            tarjamaLabel = "ترجمہ:",
            motivationSectionTitle = "اللہ کی مدد اور حوصلہ افزائی",
            nextMotivation = "اگلا",
            guideShortcutButton = "11 اقساط کی گائیڈ",
            slipResetButton = "پھسل گئے؟",
            tabHome = "مرکزی صفحہ",
            tabGuide = "11 اقساط",
            tabMilestones = "سفر اور ترتیبات",
            guideHeaderTag = "11 اقساط پر مشتمل ریکوری گائیڈ",
            guideHeaderTitle = "اللہ کی مدد سے دل و روح کی شفا",
            guideHeaderSubtitle = "کسی بھی قسط کو کھولنے کے لیے کارڈ پر ٹیپ کریں اور روحانی و جذباتی رہنمائی پڑھیں۔",
            urgeRescueTitle = "خواہش سے نجات",
            urgeRescueHeroTitle = "رک جائیں۔ اللہ آپ کی کوشش دیکھ رہا ہے۔",
            urgeRescueHeroSub = "یہ دباؤ صرف چند منٹ رہتا ہے۔ ابھی ان آسان مراحل پر عمل کریں۔",
            urgeBreathingHeader = "مرحلہ 1: 60 سیکنڈ گہرا سانس اور صبر",
            urgeTasbihHeader = "مرحلہ 2: تسبیح پڑھ کر اللہ سے مدد مانگیں",
            urgeStepsHeader = "مرحلہ 3: خواہش سے چھٹکارے کا طریقہ",
            urgeVictoryButton = "الحمدللہ، میں نے اس خواہش پر قابو پا لیا",
            appearanceCardTitle = "زبان اور پسندیدہ رنگ کی ترتیبات",
            languageLabel = "زبان منتخب کریں",
            customColorLabel = "اپنا پسندیدہ رنگ منتخب کریں",
            themeModeLabel = "روشنی / تاریکی موڈ",
            aboutCreatorTitle = "تیار کنندہ کا حوالہ",
            aboutCreatorBody = "یہ ایپلیکیشن \"Allah always with me\" محمد وقاص نے تیار کی ہے تاکہ مشکل وقت میں صبر، ذکر اور اللہ کی مدد سے نئی شروعات کی جا سکے۔",
            apkInstallTitle = "APK انسٹال اور ڈاؤن لوڈ",
            apkInstallBody = "فون پر انسٹال کرنے یا پبلش کرنے کے لیے AI Studio کے Settings مینو سے Generate APK / AAB یا Export Project استعمال کریں۔"
        )
    }
}

fun AyatWithTarjama.tarjamaFor(language: AppLanguage): String = when (language) {
    AppLanguage.ENGLISH -> englishTarjama
    AppLanguage.ROMAN_URDU -> romanUrduTarjama
    AppLanguage.URDU -> urduTarjama
}

fun AyatWithTarjama.contextFor(language: AppLanguage): String = when (language) {
    AppLanguage.ENGLISH -> helpContextEn
    AppLanguage.ROMAN_URDU -> helpContextRomanUr
    AppLanguage.URDU -> helpContextUr
}

fun DailyZikrItem.meaningFor(language: AppLanguage): String = when (language) {
    AppLanguage.ENGLISH -> meaningEn
    AppLanguage.ROMAN_URDU -> meaningRomanUr
    AppLanguage.URDU -> meaningUr
}
