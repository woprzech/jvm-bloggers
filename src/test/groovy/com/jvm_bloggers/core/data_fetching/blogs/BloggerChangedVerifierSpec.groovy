package com.jvm_bloggers.core.data_fetching.blogs

import com.jvm_bloggers.core.data_fetching.blogs.json_data.BloggerEntry
import com.jvm_bloggers.entities.blog.Blog
import com.jvm_bloggers.entities.blog.BlogType
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.LocalDateTime

import static BlogType.COMPANY
import static BlogType.PERSONAL

class BloggerChangedVerifierSpec extends Specification {

    @Subject
    BloggerChangedVerifier testObj = new BloggerChangedVerifier();

    @Unroll
    def "Should detect that BloggerEntry is different than corresponding Blog"() {
        when:
        boolean pendingChanges = testObj.pendingChanges(person, bloggerEntry)

        then:
        pendingChanges == expectedResult

        where:
        person                                 | bloggerEntry                | expectedResult
        standardPersonalBlog()                 | entryWithStandardBlogData() | false
        blogWithUppercasedRss()                | entryWithStandardBlogData() | false
        standardPersonalBlog()                 | entryWithCompanyBlogData()  | true
        blogWithDifferentJsonId()              | entryWithStandardBlogData() | true
        blogWithDifferentAuthor()              | entryWithStandardBlogData() | true
        blogWithDifferentBookmarkableId()      | entryWithStandardBlogData() | true
        blogWithDifferentRss()                 | entryWithStandardBlogData() | true
        blogWithDifferentTwitter()             | entryWithStandardBlogData() | true
        blogWithDifferentAuthorRssAndTwitter() | entryWithStandardBlogData() | true
        standardPersonalBlog()                 | entryWithDifferentPage()    | true
    }

    private BloggerEntry entryWithCompanyBlogData() {
        buildBloggerEntry(1L, "bookmarkableId", "blog", "rss", "page", "twitter", COMPANY)
    }

    private BloggerEntry entryWithDifferentPage() {
        buildBloggerEntry(1L, "bookmarkableId", "blog", "rss", "newPage", "twitter", PERSONAL)
    }

    private Blog standardPersonalBlog() {
        buildBlog(1L, "bookmarkableId", "blog", "rss", "page", "twitter")
    }

    private BloggerEntry entryWithStandardBlogData() {
        buildBloggerEntry(1L, "bookmarkableId", "blog", "rss", "page", "twitter", PERSONAL)
    }

    private Blog blogWithDifferentAuthorRssAndTwitter() {
        buildBlog(1L, "bookmarkableId","authoX", "rsX", "page", "twitteX")
    }

    private Blog blogWithDifferentBookmarkableId() {
        buildBlog(1L, "bookmarkableIdX","author", "rss", "page", "twitter")
    }

    private Blog blogWithDifferentTwitter() {
        buildBlog(1L, "bookmarkableId", "blog", "rss", "page", "twitterX")
    }

    private Blog blogWithDifferentRss() {
        buildBlog(1L, "bookmarkableId", "blog", "Xss", "page", "twitter")
    }

    private Blog blogWithDifferentAuthor() {
        buildBlog(1L, "bookmarkableId", "Author", "rss", "page", "twitter")
    }

    private Blog blogWithDifferentJsonId() {
        buildBlog(2L, "bookmarkableId", "blog", "rss", "page", "twitter")
    }

    private Blog blogWithUppercasedRss() {
        buildBlog(1L, "bookmarkableId", "blog", "RSS", "page", "twitter")
    }

    def buildBlog(Long jsonId, String bookmarkableId, String author, String rss, String pageUrl, String twitter) {
        buildBlog(jsonId, bookmarkableId, author, rss, pageUrl, twitter, LocalDateTime.now(), PERSONAL)
    }

    def buildBlog(Long jsonId, String bookmarkableId, String author, String rss, String url, String twitter, LocalDateTime dateAdded, BlogType type) {
        return Blog.builder()
            .jsonId(jsonId)
            .bookmarkableId(bookmarkableId)
            .author(author)
            .rss(rss)
            .url(url)
            .twitter(twitter)
            .dateAdded(dateAdded)
            .blogType(type)
            .build()
    }

    def buildBloggerEntry(Long jsonId, String bookmarkableId, String author, String rss, String pageUrl, String twitter, BlogType type) {
        return new BloggerEntry(jsonId, bookmarkableId, author, rss, pageUrl, twitter, type)
    }
}
