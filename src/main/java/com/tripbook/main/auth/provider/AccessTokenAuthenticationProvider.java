package com.tripbook.main.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.tripbook.main.auth.service.LoadUserService;
import com.tripbook.main.auth.token.CustomAccessToken;
import com.tripbook.main.auth.userdetails.OAuth2UserDetails;
import com.tripbook.main.member.entity.Member;
import com.tripbook.main.member.enums.MemberRole;
import com.tripbook.main.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {//AuthenticationProvider을 구현받아 authenticate와 supports를 구현해야 한다.

	private final LoadUserService loadUserService;  //restTemplate를 통해서 AccessToken을 가지고 회원의 정보를 가져오는 역할을 한다.
	private final MemberRepository memberRepository;//받아온 정보를 통해 DB에서 회원을 조회하는 역할을 한다.

	@SneakyThrows
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {//ProviderManager가 호출한다. 인증을 처리한다
		//getOAuth2UserDetails에서는 restTemplate과 AccessToken을 가지고 회원정보를 조회
		OAuth2UserDetails oAuth2User = loadUserService.getOAuth2UserDetails((CustomAccessToken) authentication);
		//중복회원가입 방지.
		userValidation(oAuth2User);

		return CustomAccessToken.builder().principal(oAuth2User).authorities(oAuth2User.getAuthorities()).build();
	}



	private void userValidation(OAuth2UserDetails oAuth2User) throws Exception {
		Member member = memberRepository.findByEmail(oAuth2User.getEmail());
		try {
			if (member != null) {
				// 중복 회원가입 방지
				log.error("Member_Already_Exists.");
				throw new BadCredentialsException("Member_Already_Exists");
			}
		}catch (Exception e){
			log.error("MemberSaveError::",e);
			throw new Exception("MemberSaveError",e);
		}
	}



	@Override
	public boolean supports(Class<?> authentication) {
		return CustomAccessToken.class.isAssignableFrom(authentication); //AccessTokenSocialTypeToken타입의  authentication 객체이면 해당 Provider가 처리한다.
	}


	/**
	 * 왜 GUEST로 설정하였나??
	 *
	 * 소셜 로그인에서 필수로 제공하는 회원의 정보들이 다 다르다.
	 * 예를 들면 카카오는 이름을 email을 필수로 제공하지 않는데, 구글은 email이 필수일 뿐더러 식별값으로 사용한다.
	 * 이렇게 소셜 로그인간의 정보의 불균형을 해소하기 위해 우리는 소셜 로그인 시에 아무런 정보도 받아오지 않고,
	 * 단지 식별값만을 사용하여 회원을 저장한다.
	 * 현재 GUEST로 저장된 상황은 클라이언트가 정보 제공에 동의를 누른 경우에 해당 코드가 실행된다.
	 *
	 * 정보 동의를 한 이후 우리는 추가 정보를 입력받으러 추가 폼 입력 화면으로 리다이렉트 시킨다.
	 * 그럼 클라이언트는 추가 정보를 입력하고 AccessToken과 함께 다시 추가 정보를 서버에 전달하면 된다.
	 *
	 * 그런데 우리는 해당 회원이 이미 가입을 한 회원인지, 아니면 정보 제공에 동의만 하고 가입은 진행하기 전 회원인지 알 수 있는 방법이 딱히 없다.
	 * 그래서 그냥 물론 다른 방법도 있겠지만 간단하게 ROLE을 GUEST로 주어서 아직 회원가입을 진행하지 않고 DB에 저장된 상태라는 것을 표시하는 것이다.
	 *
	 * (근데 지금 생각해보니까, 인가 코드를 이미 프론트엔드에서 받아오기 때문에 굳이 이럴 필요가 없는 거 같긴 하다.. 엥..ㅠㅠ 이후 제대로 실험해본 후 수정하겠다.)
	 *
	 *
	 */


}
