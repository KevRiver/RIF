package team.a501.rif.service.achievement;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import team.a501.rif.domain.achievement.Achievement;
import team.a501.rif.domain.achievement.AchievementAcq;
import team.a501.rif.domain.member.Member;
import team.a501.rif.dto.achievement.AchievementAcqResponse;
import team.a501.rif.repository.achievement.AchievementAcqRepository;
import team.a501.rif.service.member.MemberService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AchievementAcqServiceImpl implements AchievementAcqService {

    private final AchievementAcqRepository achievementAcqRepository;
    private final AchievementService achievementService;
    private final MemberService memberService;

    @Override
    @Transactional
    public AchievementAcq create(String memberId, Long achievementId) {

        Member member = memberService.findById(memberId);
        Achievement achievement = achievementService.findById(achievementId);

        AchievementAcq achievementAcq = achievementAcqRepository.save(new AchievementAcq());
        achievement.addAchievementAcq(achievementAcq);
        member.addAchievementAcq(achievementAcq);

        return achievementAcq;
    }

    @Override
    @Transactional
    public List<AchievementAcq> findByMemberId(String memberId) {

        Member member = memberService.findById(memberId);

        return achievementAcqRepository.findByMember(member);
    }

    @Override
    @Transactional
    public List<AchievementAcq> findByMemberUid(String memberUid) {

        Member member = memberService.findByUid(memberUid);

        return achievementAcqRepository.findByMember(member);
    }

    @Override
    @Transactional
    public List<AchievementAcqResponse> findOnDisplayedItemsOfMember(String memberId) {
        Member member = memberService.findById(memberId);

        return achievementAcqRepository
                .findByMemberAndOnDisplay(member, true)
                .stream()
                .map(acq ->
                        AchievementAcqResponse
                                .builder()
                                .id(acq.getId())
                                .tier(acq.getAchievement().getTier())
                                .title(acq.getAchievement().getTitle())
                                .description(acq.getAchievement().getDescription())
                                .imgPath(acq.getAchievement().getImgPath())
                                .achievedAt(acq.getCreated())
                                .build())
                .collect(Collectors.toList());
    }

}