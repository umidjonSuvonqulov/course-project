package uz.zero.gateway


fun <T> Class<T>.getIsRoutedKey(): String {
    return "${this.name}.entered"
}
