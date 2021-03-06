package com.bobray.beernextdoor.controller;

import com.bobray.beernextdoor.entity.*;
import com.bobray.beernextdoor.repository.*;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class TemplateController {

    //Repos
    @Autowired
    TypeRepository typeRepository;

    @Autowired
    BreweryRepository breweryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    UserRepository userRepository;

    @Value("${spring.datasource.salty}")
    private String salt;

    //Mappings
    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/log")
    public String getLog() {
        return "log";
    }

    @GetMapping("/fragment")
    public String getFragments() {
        return "fragment";
    }

    @GetMapping("/sign")
    public String getSign(Model out,
                          @ModelAttribute User user) {
        out.addAttribute("user", user);
        return "sign";
    }

    @PostMapping("/connexion")
    public String connexion(Model out,
                            HttpServletResponse response,
                            HttpSession session,
                            @RequestParam String nameUser,
                            @RequestParam String password) {

        Optional<User> userOptional = userRepository.findByNameUser(nameUser);
        Optional<User> userOptionalMail = userRepository.findByEmail(nameUser);
        String sha256hex = Hashing.sha256()
                .hashString(salt + password, StandardCharsets.UTF_8)
                .toString();

        if (userOptional.isPresent() || userOptionalMail.isPresent()) {
            User user;
            if (userOptional.isPresent()) {
                user = userOptional.get();
            } else {
                user = userOptionalMail.get();
            }
            if (sha256hex.equals(user.getPassword())) {
                user = getConnected(user, response);
                session.setAttribute("user", user);
                return "redirect:/type-form";
            }
        }
        out.addAttribute("logError", true);
        return "log";
    }

    @PostMapping("sign-in")
    public String signIn(Model out,
                         HttpServletResponse response,
                         HttpSession session,
                         @ModelAttribute User user,
                         @RequestParam String password,
                         @RequestParam String confirmPass) {

        out.addAttribute("user", user);
        boolean userNamePresent = false;
        boolean userMailPresent = false;
        boolean userNameBlank = false;
        boolean userMailBlank = false;
        boolean pwError = false;

        if (user.getNameUser() != null) {
            Optional<User> userOptional = userRepository.findByNameUser(user.getNameUser());
            if (userOptional.isPresent()) {
                userNamePresent = true;
            }

            userOptional = userRepository.findByEmail(user.getEmail());
            if (userOptional.isPresent()) {
                userMailPresent = true;
            }

            if (user.getNameUser().trim().equals("")) {
                userNameBlank = true;
            }

            if (user.getEmail().trim().equals("")) {
                userMailBlank = true;
            }

            if (!user.getPassword().equals(confirmPass)) {
                pwError = true;
            }

            if (userNamePresent || userMailPresent || userMailBlank || userNameBlank || pwError) {
                out.addAttribute("user", user);
                out.addAttribute("userNamePresent", userNamePresent);
                out.addAttribute("userMailPresent", userMailPresent);
                out.addAttribute("userNameBlank", userNameBlank);
                out.addAttribute("userMailBlank", userMailBlank);
                out.addAttribute("pwError", pwError);
                return "sign";
            }
            user.setApiKey(RandomStringUtils.randomAlphanumeric(20));
            user.setPassword(Hashing.sha256()
                    .hashString(salt + password, StandardCharsets.UTF_8)
                    .toString());
            user = getConnected(user, response);
            session.setAttribute("user", user);
        }
        return "redirect:/type-form";
    }

    @GetMapping("/user-profile")
    public String getProfile(Model out,
                             HttpSession session) {
        User user = (User) session.getAttribute("user");
        out.addAttribute("user", user);

        return "user-profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(Model out,
                                HttpSession session,
                                @RequestParam String nameUser,
                                @RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String confirmPass) {

        User user = (User) session.getAttribute("user");
        out.addAttribute("user", user);
        boolean pwError = false;
        boolean namePresent = false;
        boolean mailPresent = false;
        //TODO gestion de l'erreur

        if (!password.equals("")) {
            if (password.equals(confirmPass)) {
                user.setPassword(Hashing.sha256()
                        .hashString(salt + password, StandardCharsets.UTF_8)
                        .toString());
            } else {
                pwError = true;
            }
        }

        if (!nameUser.equals(user.getNameUser())) {
            Optional<User> userOptional = userRepository.findByNameUser(nameUser);
            if (userOptional.isPresent()) {
                namePresent = true;
            }
            user.setNameUser(nameUser);
        }

        if (!email.equals(user.getEmail())) {
            Optional<User> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                mailPresent = true;
            }
            user.setEmail(email);
        }

        if (pwError || namePresent || mailPresent) {
            //TODO renvoyer l'utilisateur de la session ?
            out.addAttribute("user", user);
            out.addAttribute("pwError", pwError);
            out.addAttribute("namePresent", namePresent);
            out.addAttribute("mailPresent", mailPresent);
            return "user-profile";
        }

        userRepository.saveAndFlush(user);
        session.setAttribute("user", user);
        return "redirect:/user-profile";
    }

    @GetMapping("/type-form")
    public String getTypeForm(Model out,
                              HttpSession session,
                              @ModelAttribute Type newType) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);
        out.addAttribute("newType", newType);
        return "type-form";
    }

    @PostMapping("/post-type")
    public String postNewType(Model out,
                              @ModelAttribute Type newType,
                              @RequestParam(required = false, defaultValue = "") String chooseType,
                              @RequestParam(required = false) Long typeId) {

        List<Type> types = typeRepository.findAll();
        out.addAttribute("types", types);

        if (chooseType.equals("Choisir")) {
            Optional<Type> typeOptional = typeRepository.findById(typeId);
            if (typeOptional.isPresent()) {
                newType = typeOptional.get();
                out.addAttribute("newType", newType);
            } else {
                out.addAttribute("newType", new Type());
            }
            return "type-form";
        }
        if (chooseType.equals("Go !")) {
            if (newType.getNameType() != null && !newType.getNameType().equals("")) {
                if (typeRepository.findByNameType(newType.getNameType()).isPresent()) {
                    out.addAttribute("newType", new Type());
                    out.addAttribute("typePresent", true);
                    return "type-form";
                }
                typeRepository.save(newType);
                return "redirect:/type-form";
            }
        }
        return "redirect:/type-form";
    }

    @GetMapping("/brewery-form")
    public String getBreweryForm(Model out,
                                 HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);
        out.addAttribute("newBrewery", new Brewery());
        return "brewery-form";
    }

    @PostMapping("/post-brewery")
    public String postNewBrewery(Model out,
                                 @ModelAttribute Brewery newBrewery,
                                 @RequestParam(required = false, defaultValue = "") String chooseBrewery,
                                 @RequestParam(required = false) Long breweryId) {

        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("breweries", breweries);

        if (chooseBrewery.equals("Choisir")) {
            Optional<Brewery> breweryOptional = breweryRepository.findById(breweryId);
            if (breweryOptional.isPresent()) {
                newBrewery = breweryOptional.get();
                out.addAttribute("newBrewery", newBrewery);
            } else {
                out.addAttribute("newBrewery", new Brewery());
            }
            return "brewery-form";
        }

        if (chooseBrewery.equals("Go !")) {
            if (newBrewery.getNameBrewery() != null && !newBrewery.getNameBrewery().equals("")) {
                if (breweryRepository.findByNameBrewery(newBrewery.getNameBrewery()).isPresent()) {
                    out.addAttribute("newBrewery", new Brewery());
                    out.addAttribute("breweryPresent", true);
                    return "brewery-form";
                }
                breweryRepository.save(newBrewery);
                return "redirect:/brewery-form";
            }
        }
        return "redirect:/brewery-form";
    }

    @GetMapping("/beer-form")
    public String getBeerForm(Model out,
                              HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Beer> beers = beerRepository.findAll();
        List<Type> types = typeRepository.findAll();
        List<Brewery> breweries = breweryRepository.findAll();
        out.addAttribute("beers", beers);
        out.addAttribute("newBeer", new Beer());
        out.addAttribute("types", types);
        out.addAttribute("breweries", breweries);
        return "beer-form";
    }

    @PostMapping("/post-beer")
    public String postNewBeer(Model out,
                              @ModelAttribute Beer newBeer,
                              @RequestParam(required = false, defaultValue = "") String chooseBeer,
                              @RequestParam(required = false) Long beerId) {
        //TODO refactor les erreurs pour ne faire qu'un retour
        List<Beer> beers = beerRepository.findAll();
        List<Brewery> breweries = breweryRepository.findAll();
        List<Type> types = typeRepository.findAll();
        out.addAttribute("beers", beers);
        out.addAttribute("breweries", breweries);
        out.addAttribute("types", types);

        if (chooseBeer.equals("Choisir")) {
            Optional<Beer> beerOptional = beerRepository.findById(beerId);
            if (beerOptional.isPresent()) {
                newBeer = beerOptional.get();
                out.addAttribute("newBeer", newBeer);
            } else {
                out.addAttribute("newBeer", new Beer());
            }
            return "beer-form";
        }

        boolean beerPresent = false;
        boolean breweryMissing = false;
        boolean typeMissing = false;
        boolean beerNameBlank = false;

        if (chooseBeer.equals("Go !")) {
            if (newBeer.getIdBeer() == null) {
                Optional<Beer> beerOptional = beerRepository.findByNameBeer(newBeer.getNameBeer());
                if (beerOptional.isPresent()) {
                    beerPresent = true;
                }
            }
            Optional<Brewery> breweryOptional = breweryRepository.findById(newBeer.getBrewery().getIdBrewery());
            if (breweryOptional.isPresent()) {
                Brewery currentBrewery = breweryOptional.get();
                newBeer.setBrewery(currentBrewery);
            } else {
                breweryMissing = true;
            }
            Optional<Type> typeOptional = typeRepository.findById(newBeer.getType().getIdType());
            if (typeOptional.isPresent()) {
                Type currentType = typeOptional.get();
                newBeer.setType(currentType);
            } else {
                typeMissing = true;
            }

            if (newBeer.getNameBeer().trim().equals("")) {
                beerNameBlank = true;
            }

            if (beerPresent || beerNameBlank || breweryMissing || typeMissing) {
                if (newBeer.getIdBeer() == null) {
                    out.addAttribute("newBeer", new Beer());
                } else {
                    out.addAttribute("newBeer", newBeer);
                }
                out.addAttribute("beerPresent", beerPresent);
                out.addAttribute("beerNameBlank", beerNameBlank);
                out.addAttribute("breweryMissing", breweryMissing);
                out.addAttribute("typeMissing", typeMissing);
                return "beer-form";
            }
            else {
                beerRepository.save(newBeer);
                return "redirect:/beer-form";
            }
        }
        return "redirect:/beer-form";
    }

    @GetMapping("/store-form")
    public String getStoreForm(Model out,
                               HttpSession session) {

        if (sessionCheck(session)) {
            return "redirect:/log";
        }

        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);
        out.addAttribute("newStore", new Store());
        return "store-form";
    }

    @PostMapping("/post-store")
    public String postNewStore(Model out,
                               @ModelAttribute Store newStore,
                               @RequestParam(required = false, defaultValue = "") String chooseStore,
                               @RequestParam(required = false) Long storeId) {

        List<Store> stores = storeRepository.findAll();
        out.addAttribute("stores", stores);

        if (chooseStore.equals("Choisir")) {
            Optional<Store> storeOptional = storeRepository.findById(storeId);
            if (storeOptional.isPresent()) {
                newStore = storeOptional.get();
                out.addAttribute("newStore", newStore);
            } else {
                out.addAttribute("newStore", new Store());
            }
            return "store-form";
        }

        if (chooseStore.equals("Go !")) {
            if (newStore.getNameStore() != null && !newStore.getNameStore().equals("")) {
                if (storeRepository.findByNameStore(newStore.getNameStore()).isPresent()) {
                    out.addAttribute("newStore", new Store());
                    out.addAttribute("storePresent", true);
                    return "store-form";
                }
                storeRepository.save(newStore);
            }
        }
        return "redirect:/store-form";
    }

    @GetMapping("/out")
    public String getOut(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    //Methods
    public User getConnected(User user,
                             HttpServletResponse response) {
        String sessionToken = RandomStringUtils.randomAlphanumeric(30);
        user.setToken(sessionToken);
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.MONTH, 1);
        user.setTokenExpiration(c.getTime());
        Cookie tokenSession = new Cookie("sessionId", user.getToken());
        tokenSession.setMaxAge(24 * 60 * 60);
        response.addCookie(tokenSession);
        userRepository.save(user);
        return user;
    }

    public boolean sessionCheck(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user == null;
    }
}