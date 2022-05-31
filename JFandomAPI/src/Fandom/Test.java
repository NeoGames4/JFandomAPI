package Fandom;

import FandomActivity.FandomActivity;
import FandomActivity.FandomActivityElement;
import FandomActivity.FandomActivityListener;
import FandomActivity.FandomPost;
import FandomActivity.FandomRecentChange;

public class Test {
	
	public static void main(String[] args) {
		Fandom.DEBUG = true;
		Fandom fandom = new Fandom("avatar.fandom.com/de");
		prnt("Here");
		FandomArticle article = fandom.getArticle("Aang");
		for(FandomArticleSection s : article.getSections()) System.out.println(s.title);
		prnt(FandomActivity.storageFile);
		fandom.getActivity().addListener(new FandomActivityListener() {
			
			@Override
			public void recentChangeHappened(FandomRecentChange recentChange) {
				prnt("Recent change: " + recentChange.title);
				prnt(recentChange.timestamp);
			}
			
			@Override
			public void postHappened(FandomPost post) {
				prnt("Post: " + post.title);
				prnt(post.timestamp);
			}
			
			@Override
			public void somethingHappened(FandomActivityElement activityElement) {
			}
			
		});
	}
	
	public static void prnt(Object str) {
		System.out.println(str.toString());
	}

}
