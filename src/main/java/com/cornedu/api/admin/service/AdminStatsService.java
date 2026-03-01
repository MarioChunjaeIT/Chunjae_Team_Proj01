package com.cornedu.api.admin.service;

import com.cornedu.api.board.mapper.BoardMapper;
import com.cornedu.api.faq.mapper.FaqMapper;
import com.cornedu.api.lecture.mapper.LectureMapper;
import com.cornedu.api.member.mapper.MemberMapper;
import com.cornedu.api.motherboard.mapper.MotherBoardMapper;
import com.cornedu.api.qna.mapper.QnaMapper;
import com.cornedu.api.studentboard.mapper.StudentBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminStatsService {

    private final MemberMapper memberMapper;
    private final BoardMapper boardMapper;
    private final MotherBoardMapper motherBoardMapper;
    private final StudentBoardMapper studentBoardMapper;
    private final QnaMapper qnaMapper;
    private final FaqMapper faqMapper;
    private final LectureMapper lectureMapper;

    public Map<String, Long> getStats() {
        return Map.of(
                "memberCount", memberMapper.countAll(),
                "boardCount", boardMapper.count(null, null),
                "motherBoardCount", motherBoardMapper.count(null, null),
                "studentBoardCount", studentBoardMapper.count(null, null),
                "qnaCount", qnaMapper.count(null, null),
                "faqCount", faqMapper.count(),
                "lectureCount", lectureMapper.count(null)
        );
    }
}
