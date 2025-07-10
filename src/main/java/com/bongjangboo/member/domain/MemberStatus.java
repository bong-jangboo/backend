package com.bongjangboo.member.domain;


/**
 * 회원 상태 값
 *
 * <ul>
 *   <li>ACTIVE - 정상 회원</li>
 *   <li>SLEEP - 휴면 상태</li>
 *   <li>DEACTIVATED - 탈퇴/비활성화</li>
 * </ul>
 */
public enum MemberStatus {
    ACTIVE,
    SLEEP,
    DEACTIVATED
}
