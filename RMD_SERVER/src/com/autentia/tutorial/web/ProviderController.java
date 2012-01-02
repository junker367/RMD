package com.autentia.tutorial.web;

import java.security.Provider;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/provider")
public class ProviderController {
 
    private final ProviderStore providerStore;
     
    @Inject
    public ProviderController(ProviderStore providerStore) {
        this.providerStore = providerStore;
    }
     
    @RequestMapping(method=RequestMethod.GET)
    public String getCreateForm(Model model) {
        model.addAttribute(new Provider());
        return "provider/createForm";
    }
 
    @RequestMapping(value="/names", method=RequestMethod.GET)
    public @ResponseBody List<String> getProvidersNames() {
        return providerStore.getProvidersNames();
    }
 
    @RequestMapping(value="/store", method=RequestMethod.POST)
    public @ResponseBody Long addProvider(@RequestBody Provider provider) {
        final Provider storedProvider = providerStore.sotreProvider(provider);
        return storedProvider.getId();
    }   
     
    @RequestMapping(value="/{id}", method=RequestMethod.GET)
    public @ResponseBody Provider getProvider(@PathVariable Long id) {
        final Provider provider = providerStore.loadProvider(id);
        if (provider == null) {
            throw new ResourceNotFoundException(id);
        }
        return provider; 
    }
}
