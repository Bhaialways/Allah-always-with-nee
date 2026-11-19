package com.example.content

data class RecoveryEpisode(
    val number: Int,
    val title: String,
    val subtitle: String,
    val readMinutes: Int,
    val themeTag: String,
    val verseOrHadith: String,
    val verseReference: String,
    val emotionalMessage: String,
    val allahHelpReminder: String,
    val practicalSteps: List<String>,
    val duaPrompt: String
)

data class MotivationCard(
    val id: Int,
    val title: String,
    val message: String,
    val spiritualAnchor: String,
    val reference: String
)

data class UrgeReliefStep(
    val id: String,
    val title: String,
    val detail: String
)

data class SoberMilestone(
    val days: Int,
    val title: String,
    val blessingNote: String
)

object RecoveryCurriculum {

    val milestones: List<SoberMilestone> = listOf(
        SoberMilestone(1, "First 24 Hours", "The hardest step is deciding to begin. Allah loves your first step toward purity."),
        SoberMilestone(3, "3 Days — Clear Dawn", "The initial fog begins to lift. Every hour you resisted was written in your favor."),
        SoberMilestone(7, "7 Days — First Week", "A full week of choosing dignity and Allah's pleasure over temporary impulse."),
        SoberMilestone(14, "14 Days — Renewed Focus", "Your mind is steadier, your prayers feel deeper, and self-respect is growing."),
        SoberMilestone(21, "21 Days — Rewiring Habits", "New neural pathways of self-control are taking root by Allah's permission."),
        SoberMilestone(30, "30 Days — One Month Clean", "A full month of courage, tears, and silent victories only Allah witnessed."),
        SoberMilestone(40, "40 Days — Heart Transformation", "Forty days of consistency reshape character and bring tranquil clarity."),
        SoberMilestone(90, "90 Days — Deep Freedom", "Three months of living upright. You are no longer ruled by the old cycle."),
        SoberMilestone(180, "180 Days — Half a Year", "Half a year of guarding your soul and trusting Allah in private."),
        SoberMilestone(365, "365 Days — One Full Year", "Alhamdulillah! A complete year of spiritual resilience and inner peace.")
    )

    val motivationCards: List<MotivationCard> = listOf(
        MotivationCard(
            id = 1,
            title = "You Are Never Fighting Alone",
            message = "When the urge feels heavy in a quiet room, remember that Allah is closer to you than your own heartbeat. Ask Him for strength right now—even a whispered 'Ya Allah, help me' shifts the battle.",
            spiritualAnchor = "Indeed, Allah is with those who are patient.",
            reference = "Surah Al-Baqarah 2:153"
        ),
        MotivationCard(
            id = 2,
            title = "Ten Minutes of Pain vs. Peace All Day",
            message = "An urge is like a loud storm at your window—it feels urgent, yet it peaks and fades within 10 to 15 minutes. Wait out the storm with Allah, and wake up proud tomorrow.",
            spiritualAnchor = "And whoever relies upon Allah – then He is sufficient for him.",
            reference = "Surah At-Talaq 65:3"
        ),
        MotivationCard(
            id = 3,
            title = "Shame Is Not From Allah",
            message = "If you feel broken by past strikes, know that despair is Shaytan's trap. Allah calls you back with open mercy every single time you sincerely return.",
            spiritualAnchor = "Do not despair of the mercy of Allah. Indeed, Allah forgives all sins.",
            reference = "Surah Az-Zumar 39:53"
        ),
        MotivationCard(
            id = 4,
            title = "Every Hidden Battle Counts",
            message = "Nobody else sees the moment you put your phone down or turn away from temptation—except Ar-Raqib, the All-Seeing. Nothing you sacrifice for His sake is ever wasted.",
            spiritualAnchor = "Whoever gives up something for the sake of Allah, Allah will replace it with something better.",
            reference = "Hadith (Ahmad)"
        ),
        MotivationCard(
            id = 5,
            title = "Your Future Self Is Praying For Today",
            message = "Imagine yourself 40 days from now—calm, focused, eyes bright, standing in Salah with a light heart. Today's restraint is the bridge to that version of you.",
            spiritualAnchor = "For indeed, with hardship will be ease. Indeed, with hardship will be ease.",
            reference = "Surah Ash-Sharh 94:5-6"
        ),
        MotivationCard(
            id = 6,
            title = "When Weak, Lean On Al-Qawiyy",
            message = "You do not have to be strong on your own. Call upon Al-Qawiyy (The All-Strong) and say: 'La hawla wa la quwwata illa billah'—there is no power or strength except through Allah.",
            spiritualAnchor = "Allah intends for you ease and does not intend for you hardship.",
            reference = "Surah Al-Baqarah 2:185"
        )
    )

    val urgeReliefSteps: List<UrgeReliefStep> = listOf(
        UrgeReliefStep(
            id = "step_move",
            title = "Stand Up & Leave Isolation",
            detail = "Break the physical environment immediately. Walk into the living room, step onto a balcony, or go near family."
        ),
        UrgeReliefStep(
            id = "step_wudu",
            title = "Make Fresh Wudu or Splash Cold Water",
            detail = "Cool water calms the nervous system and cools emotional heat—the Prophet (peace be upon him) taught Wudu extinguishes inner fire."
        ),
        UrgeReliefStep(
            id = "step_phone",
            title = "Lock Triggers Out of Reach for 15 Minutes",
            detail = "Close the tempting tab, put your device in another room, and give your brain 15 minutes for the dopamine wave to drop."
        ),
        UrgeReliefStep(
            id = "step_water",
            title = "Drink Cold Water & Take 10 Slow Breaths",
            detail = "Hydrate slowly and lengthen your exhale. A calmer body makes clearer spiritual choices."
        ),
        UrgeReliefStep(
            id = "step_salah",
            title = "Recite Istighfar or Pray 2 Rakaat Nafl",
            detail = "Talk honestly to Allah in Sujood: 'Ya Allah, my Nafs is pulling me, hold my heart steady for Your sake.'"
        )
    )

    val episodes: List<RecoveryEpisode> = listOf(
        RecoveryEpisode(
            number = 1,
            title = "The Turning Point: Sincere Niyyah",
            subtitle = "Why your decision today changes everything",
            readMinutes = 4,
            themeTag = "Awakening",
            verseOrHadith = "Actions are judged by intentions, and every person will have what they intended.",
            verseReference = "Sahih al-Bukhari",
            emotionalMessage = "You did not open this app by accident. Inside you is a soul tired of feeling drained, tired of silent regret, and deeply longing for peace. Real recovery begins the moment you stop trying to impress anyone else and make one quiet promise between you and Allah: 'Ya Allah, I want a clean heart so I can walk toward You without heaviness.'",
            allahHelpReminder = "Allah never mocks a sincere beginning. Even if you fell a hundred times before, the moment you turn to Him with truth, He welcomes your step.",
            practicalSteps = listOf(
                "Write your personal Niyyah title in the Challenge Setup on the Home screen.",
                "Set the exact date and time your new chapter began—own this milestone.",
                "Pray 2 Rakaat of Tawbah and ask Allah to carry what feels too heavy for you."
            ),
            duaPrompt = "Ya Allah, purify my intention and strengthen my resolve. Do not leave me to myself even for the blink of an eye."
        ),
        RecoveryEpisode(
            number = 2,
            title = "Understanding the Urge Wave",
            subtitle = "An urge is a feeling, never a command",
            readMinutes = 5,
            themeTag = "Awareness",
            verseOrHadith = "Allah does not burden a soul beyond that it can bear.",
            verseReference = "Surah Al-Baqarah 2:286",
            emotionalMessage = "When an urge hits, it whispers a lie: 'If you don't give in, this tension will never stop.' That is false. Neurologically and emotionally, an urge is like an ocean wave—it rises sharply, peaks for a few minutes, and naturally subsides if you refuse to feed it. You are not weak for feeling an urge; you become strong by watching it pass without obeying it.",
            allahHelpReminder = "Allah promised He never tests you with a wave stronger than the strength He can place inside your chest.",
            practicalSteps = listOf(
                "Next time an urge rises, tap the 'I Have an Urge' button immediately instead of negotiating.",
                "Watch the 60-second breathing timer until the urgent spike drops.",
                "Tell yourself calmly: 'This feeling will pass in 15 minutes. My self-respect stays.'"
            ),
            duaPrompt = "Ya Muqallibal Qulub, keep my heart firm when my feelings shake."
        ),
        RecoveryEpisode(
            number = 3,
            title = "Breaking the Shame Cycle",
            subtitle = "Why guilt heals, while shame destroys",
            readMinutes = 5,
            themeTag = "Mercy",
            verseOrHadith = "Say, 'O My servants who have transgressed against themselves, do not despair of the mercy of Allah.'",
            verseReference = "Surah Az-Zumar 39:53",
            emotionalMessage = "Many people stay trapped not because they love the habit, but because shame convinces them they are ruined beyond repair. Healthy guilt says: 'I made a mistake, I need Allah.' Toxic shame says: 'I am hopeless, so why try?' Recognize that voice of hopelessness for what it is—a trick to keep you away from Tawbah.",
            allahHelpReminder = "Allah loves the servant who turns back repeatedly more than the illusion of perfection. Never let a slip keep you off the prayer mat.",
            practicalSteps = listOf(
                "If a strike ever happens, refuse the 'all is lost' trap—reset immediately and do Wudu.",
                "Follow any slip immediately with a good deed: charity, Dhikr, or helping family.",
                "Speak to yourself with the mercy you would offer a struggling friend."
            ),
            duaPrompt = "Astaghfirullah al-Azim. Ya Allah, replace my heaviness with hope in Your infinite mercy."
        ),
        RecoveryEpisode(
            number = 4,
            title = "Know Your HALT Triggers",
            subtitle = "Hungry, Angry, Lonely, Tired—protect your weak hours",
            readMinutes = 5,
            themeTag = "Self-Knowledge",
            verseOrHadith = "And He found you lost and guided you.",
            verseReference = "Surah Ad-Duha 93:7",
            emotionalMessage = "Urges rarely appear out of nowhere. Most relapses strike during four emotional states: Hungry (depleted), Angry/Anxious (stressed), Lonely (disconnected), or Tired (late-night exhaustion). When you protect your sleep, step away from screens when lonely, and calm stress early, half the battle is already won before temptation even knocks.",
            allahHelpReminder = "Taking care of your body, sleep, and emotional health is an act of worship when done to stay obedient to Allah.",
            practicalSteps = listOf(
                "Identify your highest-risk hour (often late night or alone after stress).",
                "Set a strict rule: keep devices outside the bedroom after your sleep cutoff.",
                "When lonely or stressed, reach out to a friend or make Dhikr instead of scrolling."
            ),
            duaPrompt = "Ya Allah, grant me wisdom to guard my weak hours and protect my heart in solitude."
        ),
        RecoveryEpisode(
            number = 5,
            title = "Guarding the Gaze & Digital Environment",
            subtitle = "Clean the doorway before guarding the house",
            readMinutes = 5,
            themeTag = "Protection",
            verseOrHadith = "Tell the believing men to lower their gaze and guard their modesty. That is purer for them.",
            verseReference = "Surah An-Nur 24:30",
            emotionalMessage = "Willpower is like a battery—it drains fast if you stare at sparks all day. Guarding your eyes and pruning social feeds is not restriction; it is freedom. When you unfollow triggering accounts and leave digital traps behind, your heart experiences a sweetness in Iman that no temporary distraction can match.",
            allahHelpReminder = "Every time you lower your gaze for Allah, He rewards you with a light and tranquility inside your heart.",
            practicalSteps = listOf(
                "Unfollow or mute every social media page that triggers restlessness or curiosity.",
                "Never scroll aimlessly when bored—give your phone a clear purpose before unlocking it.",
                "When an accidental image appears, look away immediately and say 'Astaghfirullah'."
            ),
            duaPrompt = "Ya Allah, guard my eyes, my screen, and my thoughts, and fill my heart with modesty."
        ),
        RecoveryEpisode(
            number = 6,
            title = "Replacing Emptiness With Purpose",
            subtitle = "You cannot leave a vacuum inside the heart",
            readMinutes = 4,
            themeTag = "Growth",
            verseOrHadith = "Verily, in the remembrance of Allah do hearts find rest.",
            verseReference = "Surah Ar-Ra'd 13:28",
            emotionalMessage = "Bad habits often serve as cheap comfort for boredom, pain, or lack of direction. If you only try to 'stop' without building a life worth protecting, boredom will pull you backward. Fill your days with physical movement, learning, service to your family, and unhurried moments of Quran so your soul no longer starves for cheap thrills.",
            allahHelpReminder = "A heart occupied with purpose and Dhikr leaves very little room for whispers of temptation.",
            practicalSteps = listOf(
                "Pick one healthy daily outlet: walking, gym, reading, or skill-building.",
                "Keep a 5-minute daily Quran or Dhikr anchor right after Fajr or Maghrib.",
                "Plan your evening before sunset so idle hours do not ambush you."
            ),
            duaPrompt = "Ya Allah, occupy my time and energy with what benefits me in this life and the next."
        ),
        RecoveryEpisode(
            number = 7,
            title = "Surviving Days 7 to 14: The Quiet Storm",
            subtitle = "When initial excitement fades and boredom tests you",
            readMinutes = 5,
            themeTag = "Endurance",
            verseOrHadith = "And be patient, for indeed, Allah does not allow to be lost the reward of those who do good.",
            verseReference = "Surah Hud 11:115",
            emotionalMessage = "In the first few days, motivation is high. Around day 7 to 14, the brain craves its old familiar dopamine spike and whispers: 'Just one peek, you've been good for a week.' That whisper is the danger zone. Recognize it as a sign that your brain is healing and shedding an old dependency. Stay steady—breakthrough lives right on the other side of this wall.",
            allahHelpReminder = "Allah tests endurance precisely to raise your rank. Hold tight during boring, ordinary days.",
            practicalSteps = listOf(
                "Never negotiate 'just once' or 'just a peek'—every huge setback starts with a tiny peek.",
                "Review your Episode 1 Niyyah whenever complacency whispers.",
                "Use the Tasbih counter in Urge Rescue whenever restlessness hits."
            ),
            duaPrompt = "Rabbana afrigh 'alayna sabran—Our Lord, pour upon us patience and plant our feet firmly."
        ),
        RecoveryEpisode(
            number = 8,
            title = "The Secret Weapon of Solitude",
            subtitle = "Who you are when nobody is watching",
            readMinutes = 5,
            themeTag = "Ihsan",
            verseOrHadith = "He is with you wherever you are. And Allah, of what you do, is Seeing.",
            verseReference = "Surah Al-Hadid 57:4",
            emotionalMessage = "Ihsan means worshipping Allah as if you see Him, knowing that even if you do not see Him, He sees you. Solitude used to be your danger zone—now transform it into your honor. When you are alone and choose purity because Allah is watching, that secret integrity builds unshakeable self-confidence and deep spiritual honor.",
            allahHelpReminder = "Being faithful to Allah in secret earns you His closeness in ways public deeds rarely match.",
            practicalSteps = listOf(
                "Whenever you enter a private room, softly say 'Allah is with me' (Allahu ma'i).",
                "Avoid staying awake late alone with an unlocked phone.",
                "Make a habit of brief night Dua—even 2 minutes of honest conversation with Allah."
            ),
            duaPrompt = "Ya Allah, make my private life cleaner and more pleasing to You than my public life."
        ),
        RecoveryEpisode(
            number = 9,
            title = "Healing Emotional Wounds Without Escaping",
            subtitle = "Feeling pain without running back to old numbing habits",
            readMinutes = 6,
            themeTag = "Healing",
            verseOrHadith = "So do not weaken and do not grieve, and you will be superior if you are true believers.",
            verseReference = "Surah Ali 'Imran 3:139",
            emotionalMessage = "Often the urge is not really about desire—it is about escaping rejection, financial stress, loneliness, or deep sadness. Recovery asks us to sit honestly with uncomfortable feelings instead of numbing them. Bring your sadness straight to Allah in Sujood, just as Prophet Yaqub (AS) said: 'I only complain of my suffering and my grief to Allah.'",
            allahHelpReminder = "Al-Jabbar—The Mender of Broken Hearts—knows every tear you have never told another human being.",
            practicalSteps = listOf(
                "Before acting on an urge, ask yourself: 'Am I actually hurt, anxious, or lonely right now?'",
                "Write 2 honest sentences in your reflection note naming the real emotion.",
                "Talk to Allah plainly in your own language—He understands every unspoken ache."
            ),
            duaPrompt = "Ya Jabbar, mend what is tired inside me and teach me to seek comfort in You alone."
        ),
        RecoveryEpisode(
            number = 10,
            title = "Standing Back Up After Any Setback",
            subtitle = "Your streak number is a tool, not your worth",
            readMinutes = 5,
            themeTag = "Resilience",
            verseOrHadith = "Every son of Adam sins, and the best of those who sin are those who repent.",
            verseReference = "Sunan at-Tirmidhi",
            emotionalMessage = "Even great warriors stumble on long journeys. What defines a believer is never failing to fall—it is refusing to stay on the floor. A setback does not erase the days you fought cleanly, nor does it erase the lessons you learned. Dust yourself off immediately, record an honest reset, make Tawbah, and keep walking with Allah.",
            allahHelpReminder = "Allah rejoices over the repentance of His servant more than a traveler finding their lost lifeline in the desert.",
            practicalSteps = listOf(
                "If a slip happens, reset within 5 minutes—never allow a 'binge' mindset.",
                "Analyze what preceded the slip (time, device, emotion) and close that loophole.",
                "Thank Allah for waking your conscience up right away."
            ),
            duaPrompt = "Ya Tawwab, accept my return, forgive my weakness, and keep me steadfast from this moment forward."
        ),
        RecoveryEpisode(
            number = 11,
            title = "Living Free: Gratitude, Vigilance & Light",
            subtitle = "Walking forward with Allah always beside you",
            readMinutes = 6,
            themeTag = "Freedom",
            verseOrHadith = "If you are grateful, I will surely increase you [in favor].",
            verseReference = "Surah Ibrahim 14:7",
            emotionalMessage = "Look at how far you have walked. Freedom is not arrogance ('I'm cured forever')—freedom is humble gratitude ('Alhamdulillah, Allah carried me this far, and I still need Him every day'). Stay humble, guard your daily anchors, keep helping others gently, and live with your head held high—because Allah has always been with you, and He always will be.",
            allahHelpReminder = "Never forget who held your hand through every dark night. Say Alhamdulillah with a full heart and stay anchored to Salah and Dhikr.",
            practicalSteps = listOf(
                "Thank Allah daily for every strike-free morning and peaceful night.",
                "Stay humble—never drop the simple daily guardrails that brought you here.",
                "Revisit any episode whenever you need a reminder of who you are becoming."
            ),
            duaPrompt = "Alhamdulillahi Rabbil 'Alamin. Ya Allah, keep me grateful, humble, pure, and close to You until my final breath."
        )
    )
}
