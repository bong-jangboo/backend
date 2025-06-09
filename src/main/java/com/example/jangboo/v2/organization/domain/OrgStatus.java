package com.example.jangboo.v2.organization.domain;

public enum OrgStatus {
    PENDING,     // 등록 요청됨, 미승인 상태
    ACTIVE,      // 정상 승인된 조직
    REJECTED,    // 거절된 조직
    DEACTIVATED  // 일시 중지된 조직
}
