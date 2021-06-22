package com.codeweb.salvo;

import com.codeweb.salvo.models.*;
import com.codeweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class SalvoApplication extends SpringBootServletInitializer {

	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);



	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository, GameRepository gameRepo, GamePlayerRepository gprepository,
									  ShipRepository shipRepository, SalvoRepository salvoRepository, ScoreRepository scoreRepository) {
		return (args) -> {
			// save a couple of players
			Player player1 = playerRepository.save(new Player("j.bauer@ctu.gov",passwordEncoder.encode("123")));
			Player player2 = playerRepository.save(new Player("c.obrian@ctu.gov",passwordEncoder.encode("123")));
			Player player3 = playerRepository.save(new Player("kim_bauer@gmail.com",passwordEncoder.encode("123")));
			Player player4 = playerRepository.save(new Player("t.almeida@ctu.gov",passwordEncoder.encode("123")));

			// save a couple of games
			Game game1 = gameRepo.save(new Game(LocalDateTime.now()));
			Game game2 = gameRepo.save(new Game(LocalDateTime.now().plusHours(1)));
			Game game3 = gameRepo.save(new Game(LocalDateTime.now().plusHours(2)));
			Game game4 = gameRepo.save(new Game(LocalDateTime.now().plusHours(3)));
			Game game5 = gameRepo.save(new Game(LocalDateTime.now().plusHours(4)));

			// save a couple of GamePlayers
			GamePlayer gamePlayer1 = gprepository.save(new GamePlayer(game1,player1));
			GamePlayer gamePlayer2 = gprepository.save(new GamePlayer(game1,player2));

			GamePlayer gamePlayer3 = gprepository.save(new GamePlayer(game2,player1));
			GamePlayer gamePlayer4 = gprepository.save(new GamePlayer(game2,player2));


			GamePlayer gamePlayer5 = gprepository.save(new GamePlayer(game3,player1));
			GamePlayer gamePlayer6 = gprepository.save(new GamePlayer(game3,player3));

			GamePlayer gamePlayer7 = gprepository.save(new GamePlayer(game4,player2));
			GamePlayer gamePlayer8 = gprepository.save(new GamePlayer(game4,player3));

			//GamePlayer gamePlayer9 = gprepository.save(new GamePlayer(game5,player4));
			GamePlayer gamePlayer10 = gprepository.save(new GamePlayer(game5,player1));



			Salvo salvo1 = salvoRepository.save(new Salvo(gamePlayer1, 1, Arrays.asList("B5","C5","F1")));
			Salvo salvo2 = salvoRepository.save(new Salvo(gamePlayer1, 2, Arrays.asList("F2","D5")));

			Salvo salvo3 = salvoRepository.save(new Salvo(gamePlayer2, 1, Arrays.asList("B3","B4", "B5")));
			Salvo salvo4 = salvoRepository.save(new Salvo(gamePlayer2, 2, Arrays.asList("H1", "H3", "A2")));


			Salvo salvo5 = salvoRepository.save(new Salvo(gamePlayer3, 1, Arrays.asList("A2","A4", "G6")));
			Salvo salvo6 = salvoRepository.save(new Salvo(gamePlayer3, 2, Arrays.asList("A3","H6")));
			Salvo salvo7 = salvoRepository.save(new Salvo(gamePlayer4, 1, Arrays.asList("B5","D5", "C7")));
			Salvo salvo8 = salvoRepository.save(new Salvo(gamePlayer4, 2, Arrays.asList("C5","C6")));
			Salvo salvo9 = salvoRepository.save(new Salvo(gamePlayer5, 1, Arrays.asList("G6","H6", "A4")));
			Salvo salvo10 = salvoRepository.save(new Salvo(gamePlayer5, 2, Arrays.asList("A2","A3", "D8")));
			Salvo salvo11 = salvoRepository.save(new Salvo(gamePlayer6, 1, Arrays.asList("H1","H2", "H3")));
			Salvo salvo12 = salvoRepository.save(new Salvo(gamePlayer6, 2, Arrays.asList("E1","F2", "G3")));
			/*Salvo salvo20 = salvoRepository.save(new Salvo(gamePlayer9, 1, Arrays.asList("A1","A2", "A3")));
			Salvo salvo21 = salvoRepository.save(new Salvo(gamePlayer9, 2, Arrays.asList("G6","G7", "G8")));*/
			Salvo salvo13 = salvoRepository.save(new Salvo(gamePlayer7, 1, Arrays.asList("A3","A4", "A7")));
			Salvo salvo14 = salvoRepository.save(new Salvo(gamePlayer7, 2, Arrays.asList("A2","G6", "H6")));
			Salvo salvo22 = salvoRepository.save(new Salvo(gamePlayer10, 1, Arrays.asList("B5","B6", "C7")));
			Salvo salvo23 = salvoRepository.save(new Salvo(gamePlayer10, 2, Arrays.asList("C6","D6", "E6")));
			Salvo salvo24 = salvoRepository.save(new Salvo(gamePlayer10, 3, Arrays.asList("H1","H8")));

			//player1 game1 --- j.bauer
			Ship ship1 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer1, Arrays.asList("H2", "H3","H4") ));
			Ship ship2 = shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer1,Arrays.asList("E1","F1", "G1") ));
			Ship ship3 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer1, Arrays.asList("B4","B5") ));
            Ship shipb = shipRepository.save( new Ship(ShipType.CARRIER, gamePlayer1, Arrays.asList("D4","D5") ));
            Ship shipc = shipRepository.save( new Ship(ShipType.BATTLESHIP, gamePlayer1, Arrays.asList("B6","B7") ));

			//player 2 game1--- c.obrian
			Ship ship4 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer2, Arrays.asList("B5", "C5","D5") ));
			Ship ship5 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer2, Arrays.asList("F1","F2") ));
            Ship shipd = shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer2,Arrays.asList("D1","E1") ));
            Ship shipe = shipRepository.save( new Ship(ShipType.CARRIER, gamePlayer2, Arrays.asList("B4","B3") ));
            Ship shipf = shipRepository.save( new Ship(ShipType.BATTLESHIP, gamePlayer2, Arrays.asList("B6","B7") ));


//GAME2
//player 1 game2 --- j.bauer
			Ship ship6 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer3, Arrays.asList("B5", "C5","D5") ));
			Ship ship7 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer3, Arrays.asList("C6","C7") ));



//player 2 game2--- c.obrian
			Ship ship8 = shipRepository.save( new Ship(ShipType.SUBMARINE,gamePlayer4, Arrays.asList("A2", "A3","A4") ));
			Ship ship9 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer4, Arrays.asList("G6","H6") ));



//GAME3
//player 2 game3 --- c.obrian
			Ship ship10 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer5, Arrays.asList("B5", "C5","D5") ));
			Ship ship11 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer5, Arrays.asList("C6","C7") ));


//player 4 game3--- t.almeida
			Ship ship12= shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer6, Arrays.asList("A2", "A3","A4") ));
			Ship ship13 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer6, Arrays.asList("G6","H6") ));
//GAME4
//player 2 game4 --- c.obrian
			Ship ship14 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer7, Arrays.asList("B5", "C5","D5") ));
			Ship ship15 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer7, Arrays.asList("C6","C7") ));


//player 2 game4--- j.bauer
			Ship ship16= shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer8, Arrays.asList("A2", "A3","A4") ));
			Ship ship17 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer8, Arrays.asList("G6","H6") ));

			Salvo salvo18 = salvoRepository.save(new Salvo(gamePlayer8, 1, Arrays.asList("B5","C6", "H1")));
			Salvo salvo19 = salvoRepository.save(new Salvo(gamePlayer8, 2, Arrays.asList("C5","C7", "D5")));

//GAME5
//player 4 game5 --- t.almeida
	/*		Ship ship18 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer9, Arrays.asList("B5", "C5","D5") ));
			Ship ship19 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer9, Arrays.asList("C6","C7") ));*/


//player 2 game5 --- j.bauer
			Ship ship20= shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer10, Arrays.asList("A2", "A3","A4") ));
			Ship ship21 = shipRepository.save( new Ship(ShipType.BATTLESHIP, gamePlayer10,Arrays.asList("G6","H6") ));
//GAME6 - el solitario
//player 3 game6 --- kim_bauer
		/*	Ship ship22 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer9, Arrays.asList("B5", "C5","D5") ));
			Ship ship23 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer9,  Arrays.asList("C6","C7") ));

//GAME8
//player 3 game8 --- t.kim_bauer
			Ship ship24 = shipRepository.save( new Ship(ShipType.DESTROYER, gamePlayer9, Arrays.asList("B5", "C5","D5") ));*/
			Ship ship25 = shipRepository.save( new Ship(ShipType.PATROL_BOAT, gamePlayer7,Arrays.asList("C6","C7") ));

//player 2 game8 --- j.bauer
			Ship ship26= shipRepository.save( new Ship(ShipType.SUBMARINE, gamePlayer10, Arrays.asList("A2", "A3","A4") ));
			Ship ship27 = shipRepository.save( new Ship(ShipType.BATTLESHIP, gamePlayer10, Arrays.asList("G6","H6") ));


//Game 1
			Score score1 = scoreRepository.save(new Score(player1,game1,1,LocalDateTime.now().plusHours(2)));
			Score score2 = scoreRepository.save(new Score(player2,game1,0,LocalDateTime.now().plusHours(2)));

			//Game 2

			Score score3 =scoreRepository.save(new Score(player1,game2,0.5f,LocalDateTime.now().plusHours(2)));
			Score score4 =scoreRepository.save(new Score(player2,game2,0.5f,LocalDateTime.now().plusHours(2)));

			//Game 3
			Score score5 =scoreRepository.save(new Score(player1,game3,0.5f,LocalDateTime.now().plusHours(2)));
			Score score6 =scoreRepository.save(new Score(player3,game3,0.5f,LocalDateTime.now().plusHours(2)));

			Score score7 =scoreRepository.save(new Score(player2,game4,0.5f,LocalDateTime.now().plusHours(2)));
			Score score8 =scoreRepository.save(new Score(player3,game4,0.5f,LocalDateTime.now().plusHours(2)));


		};
	}


}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

	@Autowired
	PlayerRepository playerRepository;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(inputName-> {
			Player player = playerRepository.findByEmail(inputName);
			if (player != null) {
				return new User(player.getEmail(), player.getPassword(),
						AuthorityUtils.createAuthorityList("USER"));
			} else {
				throw new UsernameNotFoundException("Unknown user: " + inputName);
			}
		});
	}
}

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				//.antMatchers("/admin/**").hasAuthority("ADMIN")
				.antMatchers("/api/games","/api/players","/api/login","/web/**").permitAll()
				.antMatchers("/api/**","/web/game.html/","/api/game_view/**").hasAuthority("USER")
				.antMatchers("/h2-console/**").permitAll()
				.anyRequest().authenticated()
				.and().csrf().ignoringAntMatchers("/h2-console/")
				.and().headers().frameOptions().sameOrigin()
				.and()
				.formLogin()
				.usernameParameter("name")
				.passwordParameter("pwd")
				.loginPage("/api/login")
				.defaultSuccessUrl("/web/games.html")
				.and()
				.logout().logoutUrl("/api/logout");

		// turn off checking for CSRF tokens
		http.csrf().disable();

		// if user is not authenticated, just send an authentication failure response
		http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if login is successful, just clear the flags asking for authentication
		http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

		// if login fails, just send an authentication failure response
		http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

		// if logout is successful, just send a success response
		http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
	}


	private void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		}
	}


}


