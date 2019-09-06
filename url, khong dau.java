public String covertStringToURL(String str) {
   try {
       String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
       Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
       return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
   } catch (Exception e) {
       ex.printStackTrace(); 
   }
   return "";
}
