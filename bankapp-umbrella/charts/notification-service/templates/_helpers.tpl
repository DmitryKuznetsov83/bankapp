cl{{- define "notification-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "notification-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "notification-service.selectorLabels" -}}
app: {{ include "blocker-service.name" . }}
{{- end }}

{{- define "notification-service.labels" -}}
app: {{ include "notification-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}