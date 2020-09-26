package com.poc.spring.openldap.security.provider;

import com.poc.spring.openldap.security.model.UserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.InvalidNameException;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LdapUserProvider {

    private static final Logger LOG = LoggerFactory.getLogger(LdapUserProvider.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    public boolean authenticate(String username, String password) {
        return ldapTemplate.authenticate(
                "ou=users,dc=openldap,dc=spring,dc=poc,dc=com", String.format("(cn=%s)", username), password);
    }

    public UserDetails getByUsername(String username) {
        try {
            return ldapTemplate.lookup("cn=" + username + ",ou=Users,dc=openldap,dc=spring,dc=poc,dc=com", (ContextMapper<UserDetails>) ctx -> {
                DirContextAdapter context = (DirContextAdapter) ctx;
                UserDetails userDetails = new UserDetails();
                userDetails.setFullName(context.getStringAttribute("displayname"));
                userDetails.setEmail(context.getStringAttribute("mail"));
                return userDetails;
            });
        } catch (InvalidNameException | NameNotFoundException ex) {
            LOG.error(ex.getMessage());
            return null;
        }
    }

    public List<String> getUserGroups(String username) {
        return ldapTemplate.search("ou=Groups,dc=openldap,dc=spring,dc=poc,dc=com",
            String.format("uniqueMember=%s", String.format("cn=%s,ou=Users,dc=openldap,dc=spring,dc=poc,dc=com", username)),
                (ContextMapper<String>) ctx -> {
                    DirContextAdapter context = (DirContextAdapter) ctx;
                    return context.getStringAttribute("cn");
                });
    }
}
