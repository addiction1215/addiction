package com.addiction.user.users.nickname;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class RandomNicknameGenerator {

    private static final List<String> ADJECTIVES = List.of(
            "건강한", "결심한", "단단한", "맑은", "활기찬", "따뜻한", "차분한", "용감한", "성실한", "부지런한",
            "밝은", "고요한", "상냥한", "든든한", "꾸준한", "씩씩한", "새로운", "푸른", "선명한", "다정한",
            "즐거운", "행복한", "희망찬", "가벼운", "산뜻한", "정직한", "겸손한", "유쾌한", "느긋한", "재빠른",
            "반가운", "여유로운", "평온한", "깨끗한", "싱그러운", "멋진", "근사한", "기특한", "소중한", "믿음직한",
            "영리한", "슬기로운", "담백한", "순수한", "환한", "은은한", "포근한", "청량한", "쾌활한", "당찬",
            "단정한", "넉넉한", "친절한", "부드러운", "튼튼한", "반짝이는", "빛나는", "향기로운", "넓은", "깊은",
            "높은", "빠른", "느린", "조용한", "강한", "온화한", "신중한", "꼼꼼한", "대담한", "솔직한",
            "자유로운", "재미있는", "귀여운", "아늑한", "시원한", "달콤한", "상쾌한", "알찬", "풍성한", "기분좋은",
            "여린", "선한", "똑똑한", "낙천적인", "적극적인", "긍정적인", "의젓한", "활발한", "담대한", "차가운",
            "따사로운", "고운", "예쁜", "우아한", "정겨운", "순한", "깨발랄한", "싱싱한", "든든해진", "단호한",
            "침착한", "성장하는", "도전하는", "웃는", "깨어있는", "준비된", "집중한", "가뿐한", "산뜻해진", "평화로운"
    );

    private static final List<String> NOUNS = List.of(
            "쿼카", "토끼", "고래", "사자", "새벽", "하늘", "바다", "숲", "나무", "구름",
            "별", "달", "햇살", "바람", "강", "호수", "들꽃", "민들레", "해바라기", "라일락",
            "라벤더", "소나무", "대나무", "참새", "제비", "돌고래", "펭귄", "수달", "여우", "판다",
            "코알라", "기린", "얼룩말", "코끼리", "하마", "치타", "호랑이", "표범", "부엉이", "올빼미",
            "다람쥐", "고슴도치", "강아지", "고양이", "햄스터", "병아리", "나비", "꿀벌", "잠자리", "무지개",
            "노을", "아침", "저녁", "봄", "여름", "가을", "겨울", "언덕", "계곡", "섬",
            "등대", "항구", "오솔길", "정원", "마을", "샘물", "시냇물", "자갈", "모래", "파도",
            "등산화", "자전거", "연필", "공책", "책갈피", "찻잔", "머그컵", "담요", "베개", "의자",
            "창문", "문턱", "시계", "나침반", "지도", "열쇠", "우산", "가방", "편지", "엽서",
            "사과", "복숭아", "자두", "포도", "귤", "레몬", "밤", "도토리", "감자", "고구마",
            "단풍", "눈송이", "빗방울", "물결", "풀잎", "씨앗", "새싹", "꽃잎", "조약돌", "등불"
    );

    public String resolve(String nickname) {
        if (!isBlank(nickname)) {
            return nickname;
        }

        return generate();
    }

    public String generate() {
        // 난수 생성 방식 비교
        // - Random: 단순하고 이해하기 쉽지만, 싱글톤 컴포넌트에서 여러 요청 스레드가 공유하면 경합이 생길 수 있습니다.
        // - ThreadLocalRandom: 스레드별 난수 생성기를 사용하므로 서버의 동시 요청 환경에 적합하고 빠릅니다.
        // - SecureRandom: 인증번호, 임시 비밀번호, 토큰처럼 예측되면 안 되는 보안 값에 적합하지만 닉네임 생성에는 과합니다.
        // - SplittableRandom: 대량 난수 생성과 병렬 처리에 좋지만 thread-safe하지 않아 싱글톤 필드 공유에는 맞지 않습니다.
        // 현재 기능은 보안 값이 아닌 랜덤 닉네임 생성이고, 이 클래스는 Spring 싱글톤으로 공유될 수 있으므로
        // ThreadLocalRandom.current().nextInt(size)를 사용합니다. 예: size가 100이면 0~99 중 하나를 반환합니다.
        return ADJECTIVES.get(ThreadLocalRandom.current().nextInt(ADJECTIVES.size()))
                + " "
                + NOUNS.get(ThreadLocalRandom.current().nextInt(NOUNS.size()));
    }

    private boolean isBlank(String nickname) {
        if (nickname == null) {
            return true;
        }

        for (int i = 0; i < nickname.length(); i++) {
            char value = nickname.charAt(i);
            if (!Character.isWhitespace(value) && !Character.isSpaceChar(value)) {
                return false;
            }
        }
        return true;
    }
}
