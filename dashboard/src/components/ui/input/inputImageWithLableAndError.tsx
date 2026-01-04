import { Input } from "./input";
import { Label } from "../label";
import { FieldError } from "react-hook-form";
import React, { useEffect, useRef, useState } from "react";
import { Trash2 } from "lucide-react";

type IInputImageWithLabelAndErrorProps = {
    label: string;
    error?: String | undefined;
    isSingle?: boolean;
    isBorderEnable?: boolean;
    isCanDeleteImage?: boolean;
    height: number;
    initialPreviews?: string[];
    onChange?: (files: File[]) => void;
    deleteImage?: (deletedImages: string) => void;
};

export const InputImageWithLabelAndError = React.forwardRef<
    HTMLInputElement,
    IInputImageWithLabelAndErrorProps
>(({
    label,
    error,
    onChange,
    deleteImage,
    isSingle = true,
    height = 200,
    isBorderEnable = false,
    isCanDeleteImage = false,
    initialPreviews = [], ...props }, ref) => {
    const fileRef = (ref as React.RefObject<HTMLInputElement>) || useRef<HTMLInputElement>(null);
    const [isDragActive, setIsDragActive] = useState(false);
    const [previews, setPreviews] = useState<string[]>(initialPreviews);
    const createdObjectUrls = useRef<string[]>([]);

    useEffect(() => {
        if (initialPreviews.length > 0) {
            setPreviews(initialPreviews);
        }
    }, [initialPreviews]);



    useEffect(() => {
        return () => {
            createdObjectUrls.current.forEach((u) => URL.revokeObjectURL(u));
            createdObjectUrls.current = [];
        };
    }, []);

    const handleFiles = (fileList: FileList | null) => {
        if (!fileList || fileList.length === 0) return;
        const files = Array.from(fileList);
        const localPreviews = files.map((f) => {
            const u = URL.createObjectURL(f);
            createdObjectUrls.current.push(u);
            return u;
        });
        if (isSingle) {
            setPreviews(localPreviews.slice(0, 1));
        } else {
            setPreviews((p) => [...p, ...localPreviews]);
        }
        onChange && onChange(files);
    };

    const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        handleFiles(e.target.files);
    };

    const onDrop = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        setIsDragActive(false);
        handleFiles(e.dataTransfer.files);
    };

    const onDragOver = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        setIsDragActive(true);
    };

    const onDragLeave = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        setIsDragActive(false);
    };

    return (
        <div className="min-h-40 max-w-lg">
            <Label className="mb-2">{label}</Label>

            <div
                // onClick={() => fileRef.current?.click()}
                onDrop={onDrop}
                onDragOver={onDragOver}
                onDragLeave={onDragLeave}
                className={`w-full rounded-md p-4 border-2 border-dashed flex items-center justify-center cursor-pointer bg-white transition ${isDragActive ? "border-blue-400 bg-blue-50" : "border-gray-200"
                    }`}
            >
                <div className="text-center">
                    <p className="text-sm text-gray-600">Drag & drop images here</p>
                    <p className="text-xs text-gray-400 mt-1">or click to select files</p>

                    <div className="mt-3">
                        {previews?.length > 0 && (
                            <div
                                className="flex max-w-lg flex-wrap justify-center"
                                style={{ height: `${height}px` }}
                            >
                                {previews?.map((src, idx) => (
                                    <div key={idx} className={`
                                    w-28 h-28 flex-shrink-0 overflow-hidden ${isBorderEnable ? "border" : ""} rounded-2xl p-2`}>
                                       
                                       {isCanDeleteImage && (
                                           <div className="sticky top-1">
                                               <button
                                                   type="button"
                                                   onClick={() => {
                                                       if (deleteImage) {
                                                           deleteImage(src);
                                                            setPreviews([...previews.filter((p) => p !== src)]);
                                                       }
                                                   }}
                                                   className="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1"
                                               >
                                                   <Trash2 size={14} />
                                               </button>
                                           </div>
                                       )}
                                        <img src={src} alt={`preview-${idx}`} className="object-cover w-full h-full" />
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>

                </div>
            </div>

            <Input
                type={"file"}
                hidden
                accept="image/*"
                multiple
                {...props}
                ref={fileRef}
                onChange={onInputChange}
            />

            {error && (
                <p className="text-red-500 text-xs mt-2">{error}</p>
            )}


        </div>
    );
});