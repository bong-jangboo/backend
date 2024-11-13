package com.example.jangboo.accountBook.infrastructure;

import com.example.jangboo.accountBook.domain.AccountBook;
import com.example.jangboo.accountBook.domain.AccountBookStatus;
import com.example.jangboo.accountBook.domain.QAccountBook;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountBookRepositoryImpl implements AccountBookRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AccountBook> findAccountBooksByConditions(AccountBookStatus status, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable, Long deptId) {
        QAccountBook accountBook = QAccountBook.accountBook;
        BooleanBuilder builder = new BooleanBuilder();

        if (status != null) {
            builder.and(accountBook.status.eq(status));
        }
        if (fromDate != null && toDate != null) {
            builder.and(accountBook.createdAt.between(fromDate, toDate));
        }
        if (deptId != null) {
            builder.and(accountBook.deptId.eq(deptId));
        }

        List<AccountBook> results = queryFactory
                .selectFrom(accountBook)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(accountBook)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
