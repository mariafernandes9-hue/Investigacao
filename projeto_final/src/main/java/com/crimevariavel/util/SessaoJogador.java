package com.crimevariavel.util;
 
import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Jogador;
import java.util.HashMap;
import java.util.Map;
 
//gerado por ia 

import com.crimevariavel.model.Caso;
import com.crimevariavel.model.Jogador;
import java.util.HashMap;
import java.util.Map;
 
public class SessaoJogador {
    
    private static Jogador jogador;
    private static Caso casoAtual;
    private static int moedasRun;
 
    //inventário: nome do item → quantidade
    private static Map<String, Integer> inventario = new HashMap<>();
 
    //sinaliza qual item foi usado no inventário para o GameplayController reagir
    private static String itemUsadoRecentemente = null;
 
    public static void setJogador(Jogador j){
        jogador = j; }
    
    public static Jogador getJogador(){
        return jogador; }
 
    public static void setCasoAtual(Caso c){
        casoAtual = c; }
    
    public static Caso getCasoAtual(){
        return casoAtual; }
 
    public static void setMoedasRun(int m){
        moedasRun = m; }
    
    public static int getMoedasRun(){
        return moedasRun; }
         
    // adiciona 1 unidade do item ao inventário
    public static void adicionarItem(String item) {
        inventario.merge(item, 1, Integer::sum);
    }
 
    //remove 1 unidade do item (retorna false se não tinha)
    public static boolean removerItem(String item) {
        Integer qtd = inventario.get(item);
        if (qtd == null || qtd <= 0) return false;
        if (qtd == 1) inventario.remove(item);
        else inventario.put(item, qtd - 1);
        return true;
    }
 
    public static Map<String, Integer> getInventario() { return inventario; }
 
    public static void limparInventario(){
        inventario.clear(); }
 
    private static boolean modoBoss = false;
 
    public static void setModoBoss(boolean b){
        modoBoss = b; }
    
    public static boolean isModoBoss(){
        return modoBoss; }
 
    public static void setItemUsado(String item){
        itemUsadoRecentemente = item; }
    
    public static String getItemUsado(){
        return itemUsadoRecentemente; }
    
    public static void limparItemUsado(){
        itemUsadoRecentemente = null; }
}
 