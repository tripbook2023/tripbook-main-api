package com.tripbook.main.article.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tripbook.main.article.entity.Article;
import com.tripbook.main.article.enums.ArticleStatus;
import com.tripbook.main.member.entity.Member;

public interface ArticleRepository extends JpaRepository<Article, Long> {
	Slice<Article> findAllByStatus(ArticleStatus status, Pageable pageable);

	Slice<Article> findAllByStatusAndMemberIdNotIn(ArticleStatus status, List<Long> targetIds, Pageable pageable);

	List<Article> findAllByStatusAndMemberEmail(ArticleStatus status, String email);

	Page<Article> findAllByStatusAndMemberEmail(ArticleStatus status, String email, Pageable pageable);

	Long deleteArticleByMember(Member member);

	Slice<Article> findAllByTitleContainingOrContentContainingAndStatus(String title, String content,
		ArticleStatus status, Pageable pageable);

	@Query(value = "select\n"
		+ " DISTINCT new com.tripbook.main.article.entity.Article("
		+ "        a1_0.id,\n"
		+ "        a1_0.bookmarkNum,\n"
		+ "        a1_0.commentNum,\n"
		+ "        a1_0.content,\n"
		+ "        a1_0.createdAt,\n"
		+ "        a1_0.heartNum,\n"
		+ "        a1_0.isEnable,\n"
		+ "        a1_0.status,\n"
		+ "        a1_0.thumbnailUrl,\n"
		+ "        a1_0.title,\n"
		+ "        a1_0.updatedAt, \n"
		+ "        a1_0.member\n"
		+ " )"
		+ "    from\n"
		+ "        Article a1_0, \n"
		+ "        Location l1_0 \n"
		+ "    where\n"
		+ "        (a1_0.title like :title escape '\\' \n"
		+ "        or a1_0.content like :content escape '\\' \n"
		+ "        or l1_0.name like :content escape '\\') \n"
		+ "        and a1_0.status=:status \n"
		+ "        and :#{#blockIds.size()} = 0 or a1_0.member.id not in :blockIds \n"
	)
	Slice<Article> getAllByTitleContainingOrContentContainingAndStatusAndLocationNameAndNotContainBlockIds(
		@Param("title") String title,
		@Param("content") String content,
		@Param("status") ArticleStatus status,
		@Param("blockIds") List<Long> blockIds,
		Pageable pageable);

}

