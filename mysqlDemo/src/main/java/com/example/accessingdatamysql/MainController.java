package com.example.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
  @Autowired // This means to get the bean called userRepository
         // Which is auto-generated by Spring, we will use it to handle the data
  private UserRepository userRepository;

  @PostMapping(path="/add")
  // Map ONLY POST Requests
  public @ResponseBody String addNewUser (@RequestParam String name
      , @RequestParam String email) {
    // @ResponseBody means the returned String is the response, not a view name
    // @RequestParam means it is a parameter from the GET or POST request

    User n = new User();
    n.setName(name);
    n.setEmail(email);
    userRepository.save(n);
    return "Saved";
  }

  @PostMapping(path="/addAndConvertXml")
  // curl localhost:8080/demo/addAndConvertXml -d name=ThirdPerson -d email=someemail@someemailprovider.com
  public @ResponseBody String addAndConvertXml (@RequestParam String name
          , @RequestParam String email) {
    User addUser = new User();
    addUser.setName(name);
    addUser.setEmail(email);
    userRepository.save(addUser);

    try {
      // create an instance of `JAXBContext`
      JAXBContext context = JAXBContext.newInstance(User.class);

      // create an instance of `Marshaller`
      Marshaller marshaller = context.createMarshaller();

      // enable pretty-print XML output
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      // create XML file
      File file = new File("book.xml");

      // convert book object to XML file
      marshaller.marshal(addUser, file);

    } catch (JAXBException ex) {
      ex.printStackTrace();
    }

    return "Xml converted";
  }

  @GetMapping(path="/all")
  // This returns a JSON or XML with the users
  // http://localhost:8080/demo/all
  public @ResponseBody Iterable<User> getAllUsers() {
    return userRepository.findAll();
  }

  @GetMapping(path="/returnString")
  // http://localhost:8080/demo/returnString?value=display this
  public @ResponseBody String returnString(@RequestParam String value) {
    return value;
  }
}