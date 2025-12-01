import iProductVarientDto from "./iProductVarientDto";

export default interface iProductResponseDto {
    id: string,
    name: string,
    description: string,
    thumbnail: string,
    subcategory: string,
    storeName: string,
    price: number,
    productVariants: iProductVarientDto[][] | undefined
    productImages: string[]
}