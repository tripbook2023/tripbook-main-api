package com.tripbook.main.token.provider;

// public class JwtAuthenticationProvider implements AuthenticationProvider {
//
// 	@Autowired
// 	private JwtProvider jwtProvider;
//
// 	@Override
// 	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
// 		JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authentication;
// 		String token = jwtAuthenticationToken.getToken();
//
// 		try {
// 			Claims claims = jwtProvider.verify(token);
// 			String username = claims.getSubject();
// 			List<String> authorities = (List<String>)claims.get("authorities");
//
// 			List<GrantedAuthority> grantedAuthorities = authorities.stream()
// 				.map(SimpleGrantedAuthority::new)
// 				.collect(Collectors.toList());
//
// 			return new UsernamePasswordAuthenticationToken(username, token, grantedAuthorities);
// 		} catch (Exception e) {
// 			throw new BadCredentialsException("Invalid token");
// 		}
// 	}
//
// 	@Override
// 	public boolean supports(Class<?> authentication) {
// 		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
// 	}
//
// }
