package com.generated.app.data;

import com.generated.app.model.LyricLine;
import com.generated.app.model.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LyricsRepository {

    private static LyricsRepository instance;

    public static synchronized LyricsRepository getInstance() {
        if (instance == null) {
            instance = new LyricsRepository();
        }
        return instance;
    }

    private final List<Track> demoTracks = new ArrayList<>();

    private LyricsRepository() {
        initDemoTracks();
    }

    private void initDemoTracks() {
        // Track 1: Viva La Vida - Coldplay (Hebrew Translation)
        List<LyricLine> vivaLines = new ArrayList<>();
        vivaLines.add(new LyricLine(1, 0, "I used to rule the world", "נהגתי לשלוט בעולם"));
        vivaLines.add(new LyricLine(2, 4000, "Seas would rise when I gave the word", "ימים היו גועשים כשנתתי פקודה"));
        vivaLines.add(new LyricLine(3, 8500, "Now in the morning I sleep alone", "עכשיו בבוקר אני ישן לבדי"));
        vivaLines.add(new LyricLine(4, 13000, "Sweep the streets I used to own", "מטאטא את הרחובות שהיו בעבר שלי"));
        vivaLines.add(new LyricLine(5, 17500, "I used to roll the dice", "נהגתי להטיל את הקוביות"));
        vivaLines.add(new LyricLine(6, 21500, "Feel the fear in my enemy's eyes", "לראות את הפחד בעיני אויבי"));
        vivaLines.add(new LyricLine(7, 26000, "Listen as the crowd would sing", "להקשיב להמון שהיה שר:"));
        vivaLines.add(new LyricLine(8, 30500, "Now the old king is dead, long live the king!", "המלך הישן מת, יחי המלך החדש!"));
        vivaLines.add(new LyricLine(9, 35000, "One minute I held the key", "רגע אחד החזקתי במפתח"));
        vivaLines.add(new LyricLine(10, 39500, "Next the walls were closed on me", "וברגע הבא החומות נסגרו עליי"));
        vivaLines.add(new LyricLine(11, 44000, "And I discovered that my castles stand", "וגיליתי שארמונותיי ניצבים"));
        vivaLines.add(new LyricLine(12, 48500, "Upon pillars of salt and pillars of sand", "על עמודי מלח ומשענות של חול"));
        vivaLines.add(new LyricLine(13, 53000, "I hear Jerusalem bells a-ringin'", "אני שומע את פעמוני ירושלים מצלצלים"));
        vivaLines.add(new LyricLine(14, 57500, "Roman cavalry choirs are singing", "מקהלות פרשים רומיים שרות"));
        vivaLines.add(new LyricLine(15, 62000, "Be my mirror, my sword and shield", "היי לי למראה, לחרב ולמגן"));
        vivaLines.add(new LyricLine(16, 66500, "My missionaries in a foreign field", "שליחיי בארץ זרה ונשכחת"));
        vivaLines.add(new LyricLine(17, 71000, "For some reason I can't explain", "מסיבה שאינני מסוגל להסביר"));
        vivaLines.add(new LyricLine(18, 75500, "Once you'd gone there was never", "ברגע שהלכת, לעולם לא חזר"));
        vivaLines.add(new LyricLine(19, 80000, "Never an honest word", "אף לא דבר אמת אחד"));
        vivaLines.add(new LyricLine(20, 84500, "And that was when I ruled the world", "וזה קרה כשאני שלטתי בעולם"));
        demoTracks.add(new Track("Viva La Vida", "Coldplay", 95000, vivaLines));

        // Track 2: Shape of You - Ed Sheeran
        List<LyricLine> shapeLines = new ArrayList<>();
        shapeLines.add(new LyricLine(1, 0, "The club isn't the best place to find a lover", "המועדון הוא לא המקום הטוב למצוא אהבה"));
        shapeLines.add(new LyricLine(2, 4500, "So the bar is where I go", "אז הבר הוא המקום שאליו אני הולך"));
        shapeLines.add(new LyricLine(3, 9000, "Me and my friends at the table doing shots", "אני וחבריי סביב השולחן מרימים כוסות"));
        shapeLines.add(new LyricLine(4, 13500, "Drinking fast and then we talk slow", "שותים מהר ואז מדברים לאט"));
        shapeLines.add(new LyricLine(5, 18000, "Come over and start up a conversation with just me", "גשי אליי והתחילי שיחה רק איתי"));
        shapeLines.add(new LyricLine(6, 22500, "And trust me I'll give it a chance now", "ותסמכי עליי שאתן לזה הזדמנות"));
        shapeLines.add(new LyricLine(7, 27000, "Take my hand, stop, put Van the Man on the jukebox", "אחזי בידי, עצרי, ושימי שיר בתיבת הנגינה"));
        shapeLines.add(new LyricLine(8, 31500, "And then we start to dance, and now I'm singing like", "ואז נתחיל לרקוד, ועכשיו אני שר כך:"));
        shapeLines.add(new LyricLine(9, 36000, "Girl, you know I want your love", "ילדה, את יודעת שאני רוצה באהבתך"));
        shapeLines.add(new LyricLine(10, 40500, "Your love was handmade for somebody like me", "אהבתך נתפרה במיוחד עבור אחד כמוני"));
        shapeLines.add(new LyricLine(11, 45000, "Come on now, follow my lead", "בואי עכשיו, לכי בעקבותיי"));
        shapeLines.add(new LyricLine(12, 49500, "I may be crazy, don't mind me", "אולי אני משוגע, אל תשימי לב"));
        shapeLines.add(new LyricLine(13, 54000, "I'm in love with the shape of you", "אני מאוהב בדמותך ובקווייך"));
        demoTracks.add(new Track("Shape of You", "Ed Sheeran", 65000, shapeLines));

        // Track 3: Shallow - Lady Gaga & Bradley Cooper
        List<LyricLine> shallowLines = new ArrayList<>();
        shallowLines.add(new LyricLine(1, 0, "Tell me somethin', girl", "הגידי לי משהו, נערה"));
        shallowLines.add(new LyricLine(2, 5000, "Are you happy in this modern world?", "האם את מאושרת בעולם המודרני הזה?"));
        shallowLines.add(new LyricLine(3, 10500, "Or do you need more?", "או שמא את זקוקה ליותר?"));
        shallowLines.add(new LyricLine(4, 15000, "Is there somethin' else you're searchin' for?", "האם יש משהו נוסף שאת מחפשת?"));
        shallowLines.add(new LyricLine(5, 20000, "I'm falling", "אני צולל ונופל"));
        shallowLines.add(new LyricLine(6, 25000, "In all the good times I find myself longin' for change", "בכל הרגעים הטובים אני כמהה לשינוי"));
        shallowLines.add(new LyricLine(7, 32000, "And in the bad times I fear myself", "וברגעים הרעים אני פוחד מעצמי"));
        shallowLines.add(new LyricLine(8, 39000, "Tell me something, boy", "הגד לי משהו, בחור"));
        shallowLines.add(new LyricLine(9, 44000, "Aren't you tired tryin' to fill that void?", "האם אינך עייף מניסיונות למלא את הריק?"));
        shallowLines.add(new LyricLine(10, 49000, "I'm off the deep end, watch as I dive in", "אני קופצת למים העמוקים, הבט בי צוללת"));
        shallowLines.add(new LyricLine(11, 56000, "I'll never meet the ground", "לעולם לא אפגע בקרקעית"));
        shallowLines.add(new LyricLine(12, 62000, "Crash through the surface, where they can't hurt us", "מבקעת את פני המים, שם אינם יכולים לפגוע בנו"));
        shallowLines.add(new LyricLine(13, 69000, "We're far from the shallow now", "אנחנו רחוקים ממים רדודים עכשיו"));
        demoTracks.add(new Track("Shallow", "Lady Gaga & Bradley Cooper", 80000, shallowLines));
    }

    public List<Track> getDemoTracks() {
        return Collections.unmodifiableList(demoTracks);
    }

    public Track findTrackOrGenerate(String title, String artist) {
        if (title == null) return demoTracks.get(0);
        for (Track track : demoTracks) {
            if (track.getTitle().equalsIgnoreCase(title.trim()) ||
                title.toLowerCase().contains(track.getTitle().toLowerCase())) {
                return track;
            }
        }
        // Generate simulated lyrics with translated Hebrew lines if track is unknown
        List<LyricLine> genLines = new ArrayList<>();
        genLines.add(new LyricLine(1, 0, "Listening to " + title, "מאזין לשיר: " + title));
        genLines.add(new LyricLine(2, 6000, "By artist " + artist, "ביצוע של האמן: " + artist));
        genLines.add(new LyricLine(3, 12000, "Music plays in the background", "המוזיקה ממשיכה להתנגן ברקע"));
        genLines.add(new LyricLine(4, 18000, "Hebrew translation synchronizes live", "התרגום לעברית מסתנכרן בזמן אמת"));
        genLines.add(new LyricLine(5, 25000, "Enjoy the melody and rhythm", "תהנו מהלחן ומהמקצב השוטף"));
        genLines.add(new LyricLine(6, 33000, "The words convey the spirit of song", "המילים מעבירות את רוח השיר"));
        genLines.add(new LyricLine(7, 42000, "Sync keeps pace with Spotify playback", "הסנכרון ממשיך בהתאם לקצב ההשמעה"));
        return new Track(title, artist, 60000, genLines);
    }
}
