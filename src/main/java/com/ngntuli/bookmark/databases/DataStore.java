package com.ngntuli.bookmark.databases;

import com.ngntuli.bookmark.models.Bookmark;
import com.ngntuli.bookmark.models.User;
import com.ngntuli.bookmark.models.UserBookmark;
import com.ngntuli.bookmark.services.BookmarkService;
import com.ngntuli.bookmark.services.UserService;
import com.ngntuli.bookmark.utilities.BookGenre;
import com.ngntuli.bookmark.utilities.BookmarkData;
import com.ngntuli.bookmark.utilities.Gender;
import com.ngntuli.bookmark.utilities.IOUtil;
import com.ngntuli.bookmark.utilities.MovieGenre;
import com.ngntuli.bookmark.utilities.UserType;

public class DataStore {

	private static User[] users = new User[BookmarkData.TOTAL_USER_COUNT];

	public static User[] getUsers() {
		return users;
	}

	private static Bookmark[][] bookmarks = new Bookmark[BookmarkData.BOOMARK_TYPES_COUNT][BookmarkData.BOOKMARK_COUNT_PER_TYPE];

	public static Bookmark[][] getBookmarks() {
		return bookmarks;
	}

	private static UserBookmark[] userBookmarks = new UserBookmark[BookmarkData.TOTAL_USER_COUNT
			* BookmarkData.USER_BOOKMARK_LIMIT];
	private static int bookmarkIndex;

	public static void loadData() {
		loadUsers();
		loadWebLinks();
		loadMovies();
		loadBooks();
	}

	private static void loadUsers() {

		String[] data = new String[BookmarkData.TOTAL_USER_COUNT];
		IOUtil.read(data, "User");
		int rowNum = 0;
		for (String row : data) {
			String[] values = row.split(";");

			Gender gender = Gender.MALE;
			if (values[5].equals("f")) {
				gender = Gender.FEMALE;
			} else if (values[5].equals("t")) {
				gender = Gender.TRANSGENDER;
			}

			users[rowNum++] = UserService.getInstance().createUser(Long.parseLong(values[0]), values[1], values[2],
					values[3], values[4], gender, values[6]);
		}
	}

	private static void loadWebLinks() {

		String[] data = new String[BookmarkData.BOOKMARK_COUNT_PER_TYPE];
		IOUtil.read(data, "WebLink");
		int colNum = 0;
		for (String row : data) {
			String[] values = row.split(";");
			bookmarks[0][colNum++] = BookmarkService.getInstance().createWebLink(Long.parseLong(values[0]), values[1],
					values[2], values[3]/* , values[4] */);
		}
	}

	private static void loadMovies() {

		String[] data = new String[BookmarkData.BOOKMARK_COUNT_PER_TYPE];
		IOUtil.read(data, "Movie");
		int colNum = 0;
		for (String row : data) {
			String[] values = row.split(";");
			String[] cast = values[3].split(",");
			String[] directors = values[4].split(",");
			bookmarks[1][colNum++] = BookmarkService.getInstance().createMovie(Long.parseLong(values[0]), values[1], "",
					Integer.parseInt(values[2]), cast, directors, MovieGenre.valueOf(values[5]),
					Double.parseDouble(values[6])/* , values[7] */);
		}
	}

	private static void loadBooks() {

		String[] data = new String[BookmarkData.BOOKMARK_COUNT_PER_TYPE];
		IOUtil.read(data, "Book");
		int colNum = 0;
		for (String row : data) {
			String[] values = row.split(";");
			String[] authors = values[4].split(",");
			bookmarks[2][colNum++] = BookmarkService.getInstance().createBook(Long.parseLong(values[0]), values[1],
					Integer.parseInt(values[2]), values[3], authors, BookGenre.valueOf(values[5]),
					Double.parseDouble(values[6])/* , values[7] */);
		}
	}

	public static void add(UserBookmark userBookmark) {
		userBookmarks[bookmarkIndex] = userBookmark;
		bookmarkIndex++;

	}
}
