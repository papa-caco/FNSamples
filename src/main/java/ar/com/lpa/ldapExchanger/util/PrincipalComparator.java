package ar.com.lpa.ldapExchanger.util;

import java.util.Comparator;

import ar.com.lpa.ldapExchanger.model.Principal;

public class PrincipalComparator implements Comparator<Principal> {
    @Override
    public int compare(Principal p1, Principal p2) {
        int typeComparison = p1.getPrincipalType().compareTo(p2.getPrincipalType());
        if (typeComparison != 0) {
            return typeComparison;
        }
        if (p1.getSamAccountName() == null && p2.getSamAccountName() == null) {
            return 0; // Ambos nulos, son iguales
        }
        if (p1.getSamAccountName() == null) {
            return -1; // p1 es null, va primero
        }
        if (p2.getSamAccountName() == null) {
            return 1; // p2 es null, va primero
        } // Si ambos no son null, compararlos alfabeticamente
        return p1.getSamAccountName().compareTo(p2.getSamAccountName());
    }
}