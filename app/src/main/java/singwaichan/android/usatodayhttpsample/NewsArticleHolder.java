package singwaichan.android.usatodayhttpsample;

/**
 * Created by Singwai Chan on 1/31/15.
 */
public class NewsArticleHolder {

    //Since there is no list. This is not needed.
    public static class ArticleHolder {

    }

    public static class ArticleItem {

        private String description;
        private String link;
        private String pubDate;
        private String title;

        public ArticleItem(String description, String link, String date, String title) {
            this.description = description;
            this.link = link;
            this.pubDate = date;
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public String getLink() {
            return link;
        }

        public String getPubDate() {
            return pubDate;
        }

        public String getTitle() {
            return title;
        }
    }
}
